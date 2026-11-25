# Lab 10: Capstone Milestone 3 — RESTful CRUD API

**Course:** WEBDEV2
**Week:** 10
**Primary capstone resource:** `Pet`
**Lecture assessment:** LA 10: RESTful API Development Quiz
**Estimated duration:** 3 hours

## Task 1 — URL design

Base URL: `/api/pets`

| Method | Path | Purpose | Success response | Other outcomes |
|---|---|---|---|---|
| GET | `/api/pets` | List pets | `200 OK`, JSON array (including `[]` when empty) | — |
| GET | `/api/pets/{id}` | Read one pet | `200 OK`, `PetResponse` | `404 Not Found` |
| POST | `/api/pets` | Create a pet | `201 Created`, `Location: /api/pets/{id}`, `PetResponse` | `400 Bad Request`, `409 Conflict` |
| PUT | `/api/pets/{id}` | Replace all editable fields | `200 OK`, updated `PetResponse` | `400 Bad Request`, `404 Not Found`, `409 Conflict` |
| PATCH | `/api/pets/{id}` | Update supplied fields only | `200 OK`, updated `PetResponse` | `400 Bad Request`, `404 Not Found`, `409 Conflict` |
| DELETE | `/api/pets/{id}` | Delete one pet | `204 No Content` | `404 Not Found` |

`PetController` implements all six method/URL combinations. It returns DTOs only; no JPA entity is accepted or returned by a controller method.

## Tasks 2–5 — DTOs and behavior

The API uses these request/response DTOs:

- `CreatePetRequest`
- `UpdatePetRequest`
- `PetResponse`
- `ApiError`

`PetResponse` contains only `id`, `microchipNumber`, `name`, `species`, and `breed`. `PetService` maps JPA entities to `PetResponse` before returning data to the controller.

- GET-all returns `200` with an empty JSON array when there are no pets.
- GET-by-ID and all update/delete operations return `404` for an unknown ID.
- POST validates required fields and returns `201` with a `Location` header.
- PUT requires and replaces all four editable fields.
- PATCH updates only non-null fields; blank supplied text is rejected with `400`.
- A duplicate microchip number returns `409 Conflict` with an `ApiError` response.
- DELETE returns `204`; repeating the same DELETE returns `404`.

## Verification evidence

Run the integration tests from the repository root:

```powershell
.\mvnw.cmd test
```

`PetRestApiIntegrationTests.completeCrudWorkflowReturnsExpectedStatusesAndLocation` exercises create (`201` + `Location`), list and read (`200`), replace (`200`), partial update (`200` while preserving other fields), delete (`204`), repeat delete (`404`), and read-after-delete (`404`). `duplicateMicrochipReturnsConflictAndInvalidCreateReturnsBadRequest` verifies the `409` duplicate-microchip response and a `400` invalid-create response. Both tests use an isolated in-memory H2 database.

Example duplicate request:

```http
POST /api/pets
Content-Type: application/json

{"microchipNumber":"CHIP-W10-DUP","name":"Milo","species":"Cat","breed":"Domestic"}
```

When that microchip number already exists, the API responds:

```http
HTTP/1.1 409 Conflict
Content-Type: application/json

{"message":"A pet with that microchip number already exists."}
```

## Submission

This Week 10 project and its required Spring Boot configuration are on the `week10` branch. Push the branch with `git push -u origin week10`; submit the repository URL in Moodle as instructed.
