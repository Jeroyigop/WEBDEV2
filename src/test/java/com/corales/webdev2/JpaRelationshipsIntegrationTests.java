package com.corales.webdev2;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.hibernate.LazyInitializationException;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManagerFactory;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:week9-relationships;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "spring.jpa.properties.hibernate.generate_statistics=true"
})
class JpaRelationshipsIntegrationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeReadService employeeReadService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PetTagRepository petTagRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private Statistics statistics;

    @BeforeEach
    void resetStatistics() {
        statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        statistics.clear();
    }

    @Test
    void userOwnsProfileAndDepartmentPersistsEmployees() {
        User user = new User();
        user.setUsername("week9_user");
        user.setEmail("week9@example.com");
        user.setProfile(new UserProfile("555-0101", "Lab 9"));
        User savedUser = userRepository.saveAndFlush(user);

        assertThat(savedUser.getProfile().getId()).isNotNull();
        assertThat(userProfileRepository.findById(savedUser.getProfile().getId())).isPresent();

        Department department = new Department("Adoptions");
        department.addEmployee(new Employee("Alex"));
        department.addEmployee(new Employee("Sam"));
        departmentRepository.saveAndFlush(department);

        Department readBack = departmentRepository.findAll().stream()
                .filter(item -> item.getName().equals("Adoptions"))
                .findFirst()
                .orElseThrow();
        assertThat(readBack.getEmployees()).extracting(Employee::getName)
                .containsExactlyInAnyOrder("Alex", "Sam");
    }

    @Test
    void lazyLoadingFailsOutsideTransactionAndBothReadFixesWork() {
        Department department = new Department("Veterinary");
        Employee employee = new Employee("Riley");
        department.addEmployee(employee);
        departmentRepository.saveAndFlush(department);
        Long employeeId = employee.getId();

        statistics.clear();
        Throwable lazyFailure = catchThrowable(() -> employeeReadService
                .loadWithoutDepartmentFetch(employeeId).getDepartment().getName());
        assertThat(lazyFailure).isInstanceOf(LazyInitializationException.class);
        System.out.println("Expected lazy failure: " + lazyFailure.getClass().getName() + ": "
                + lazyFailure.getMessage());
        if (lazyFailure.getStackTrace().length > 0) {
            System.out.println("  at " + lazyFailure.getStackTrace()[0]);
        }
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
        long lazyQueryCount = statistics.getPrepareStatementCount();

        statistics.clear();
        assertThat(employeeReadService.readDepartmentNameTransactionally(employeeId))
                .isEqualTo("Veterinary");
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(2);
        long transactionalQueryCount = statistics.getPrepareStatementCount();

        statistics.clear();
        Employee fetchedEmployee = employeeReadService.loadWithDepartmentFetch(employeeId);
        assertThat(fetchedEmployee.getDepartment().getName()).isEqualTo("Veterinary");
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
        System.out.printf("Employee read SQL statements: lazy failure=%d, transactional=%d, fetch join=%d%n",
                lazyQueryCount, transactionalQueryCount, statistics.getPrepareStatementCount());
    }

    @Test
    void orphanRemovalDeletesEmployeeAndStudentRemovalPreservesSharedCourse() {
        Department department = new Department("Clinic");
        Employee employee = new Employee("Morgan");
        department.addEmployee(employee);
        departmentRepository.saveAndFlush(department);

        department.removeEmployee(employee);
        departmentRepository.saveAndFlush(department);
        assertThat(employeeRepository.findById(employee.getId())).isEmpty();

        Course sharedCourse = courseRepository.saveAndFlush(new Course("Animal Care"));
        Student firstStudent = new Student("Taylor");
        firstStudent.enrollIn(sharedCourse, LocalDate.of(2026, 10, 29), "A");
        Student savedStudent = studentRepository.saveAndFlush(firstStudent);
        Student secondStudent = new Student("Jordan");
        secondStudent.enrollIn(sharedCourse, LocalDate.of(2026, 10, 29), "B");
        studentRepository.saveAndFlush(secondStudent);

        studentRepository.deleteById(savedStudent.getId());
        studentRepository.flush();
        assertThat(courseRepository.findById(sharedCourse.getId())).isPresent();
        assertThat(enrollmentRepository.countByCourse_Id(sharedCourse.getId())).isEqualTo(1);
    }

    @Test
    void duplicateStudentCourseEnrollmentIsRejected() {
        Course course = courseRepository.saveAndFlush(new Course("Shelter Management"));
        Student student = new Student("Casey");
        student.enrollIn(course, LocalDate.of(2026, 10, 29), "B");
        Student savedStudent = studentRepository.saveAndFlush(student);
        assertThat(enrollmentRepository.countByStudent_IdAndCourse_Id(savedStudent.getId(), course.getId()))
                .isEqualTo(1);

        Enrollment duplicate = new Enrollment(savedStudent, course, LocalDate.of(2026, 10, 30), "A");
        assertThatThrownBy(() -> enrollmentRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThat(enrollmentRepository.countByStudent_IdAndCourse_Id(savedStudent.getId(), course.getId()))
                .isEqualTo(1);
    }

    @Test
    void petShelterCapstoneSavesAndReadsOwnerAndAttributedPetTagGraph() {
        PetOwner owner = new PetOwner("Jamie", "jamie@example.com");
        Pet pet = new Pet("CHIP-W9-001", "Clover", "Rabbit", "Holland Lop");
        owner.addPet(pet);
        PetOwner savedOwner = petOwnerRepository.saveAndFlush(owner);
        Tag tag = tagRepository.saveAndFlush(new Tag("Needs follow-up"));

        PetTag assignment = pet.addTag(tag, "2026-10-29", "Schedule a wellness check");
        petTagRepository.saveAndFlush(assignment);

        Pet readBack = petRepository.findWithOwnerById(pet.getId()).orElseThrow();
        assertThat(readBack.getOwner().getName()).isEqualTo("Jamie");
        assertThat(petOwnerRepository.findWithPetsById(savedOwner.getId()).orElseThrow().getPets())
                .extracting(Pet::getName)
                .contains("Clover");
        PetTag savedAssignment = petTagRepository.findWithPetAndTagById(assignment.getId()).orElseThrow();
        assertThat(savedAssignment.getPet().getId()).isEqualTo(pet.getId());
        assertThat(savedAssignment.getTag().getName()).isEqualTo("Needs follow-up");
        assertThat(savedAssignment.getNotes()).isEqualTo("Schedule a wellness check");
    }

    @Test
    @Transactional
    void userProfileAndDepartmentRelationshipsHaveBothSidesLinked() {
        User user = new User();
        user.setUsername("week9_bidirectional");
        user.setEmail("linked@example.com");
        UserProfile profile = new UserProfile("555-0102", "Linked address");
        user.setProfile(profile);

        Department department = new Department("Intake");
        Employee employee = new Employee("Avery");
        department.addEmployee(employee);

        assertThat(profile.getUser()).isSameAs(user);
        assertThat(employee.getDepartment()).isSameAs(department);
    }
}
