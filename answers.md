# Lab 11 — Capstone Milestone 3: Complete Core REST API

**Course:** Web Development 2
**Week:** 11
**Primary resource:** Pet
**Branch:** `week11`, created from the cleaned `week10` capstone branch

## Task 1 — Request validation

`CreatePetRequest` validates all required values with meaningful `@NotBlank` messages and limits each string using `@Size`. `UpdatePetRequest` applies length limits to optional fields. `@Valid` is applied to POST, PUT, and PATCH request bodies.

Invalid POST example:

```http
POST /api/pets
Content-Type: application/json

{"microchipNumber":"CHIP-W11-INVALID","name":"","species":"Rabbit","breed":"Holland Lop"}
```

Response: `400 Bad Request`

```json
{
  "status": 400,
  "message": "Request validation failed.",
  "timestamp": "2026-11-25T02:00:00Z",
  "errors": [
    {"field":"name","message":"Pet name is required."}
  ]
}
```

## Task 2 — Global exception handling

`GlobalExceptionHandler` handles Bean Validation failures, `ResourceNotFoundException`, conflicts, malformed bodies, invalid request parameters, and unexpected exceptions. All responses share the `ErrorResponse` fields `status`, `message`, `timestamp`, and nullable `errors`. Field-level validation details use `ApiFieldError`.

Example not-found request: `GET /api/pets/0`
Expected response: `404` and an `ErrorResponse` with `status: 404`.

## Task 3 — CORS

`WebConfig` enables CORS for `/api/**` from `http://localhost:3000`, allows standard API methods including OPTIONS, allows all headers, and permits credentials. `PetRestApiIntegrationTests.corsAllowsConfiguredFrontendOrigin` verifies the preflight response includes `Access-Control-Allow-Origin` and credentials headers.

Manual check:

```powershell
curl.exe -i -X OPTIONS http://localhost:8080/api/pets -H "Origin: http://localhost:3000" -H "Access-Control-Request-Method: POST"
```

## Task 4 — Pagination and filtering

`GET /api/pets` returns a Spring `Page<PetResponse>`. Query parameters are `page` (default `0`), `size` (default `20`, maximum `100`), `sort` (default `id`), `direction` (default `asc`), and optional `species`. Sorting accepts `name,asc` or a field plus the separate `direction` parameter. Supported sort fields are `id`, `microchipNumber`, `name`, `species`, and `breed`.

Examples:

- `GET /api/pets?page=0&size=5&sort=name,asc`
- `GET /api/pets?species=Rabbit`
- `GET /api/pets?size=999` returns page metadata with `size: 100`.

## Task 5 — Endpoint documentation and runnable collection

- [API.md](./API.md) documents the CRUD endpoints, query parameters, request fields, response codes, examples, validation errors, and CORS.
- [api_tests.json](./api_tests.json) is the Postman v2.1 collection **Week11 - WEBDEV2 Pet API**. It covers create, paginated list, species filtering, read, replace, patch, duplicate conflict, validation failure, not found, and delete.
- The collection generates a fresh microchip value for each run and stores the created pet ID for subsequent requests. Import it into Postman and run the collection against the app at `http://localhost:8080`.

## Verification

Run all project integration tests:

```powershell
.\mvnw.cmd test
```

`PetRestApiIntegrationTests` covers the Week 10 CRUD workflow plus field-level validation, consistent not-found/conflict error bodies, pagination and metadata, species filtering, the 100-item cap, invalid page handling, and CORS preflight.

## Submission

Week 11 files are at the repository root on the `week11` branch. Push with `git push -u origin week11`, then submit the repository URL in Moodle.
