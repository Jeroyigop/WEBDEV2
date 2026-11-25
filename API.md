# Pet REST API

Base URL: `http://localhost:8080`
Resource: `/api/pets`

The API uses JSON. Pet responses expose `id`, `microchipNumber`, `name`, `species`, and `breed`; JPA entities are not returned directly. Error responses use this shape:

```json
{
  "status": 404,
  "message": "Pet with ID 0 was not found.",
  "timestamp": "2026-11-25T02:00:00Z",
  "errors": null
}
```

Validation errors include field-level details:

```json
{
  "status": 400,
  "message": "Request validation failed.",
  "timestamp": "2026-11-25T02:00:00Z",
  "errors": [
    { "field": "name", "message": "Pet name is required." }
  ]
}
```

## Endpoints

### List pets

`GET /api/pets`

Returns a page of pets; filtering by species is optional.

| Query parameter | Type | Default | Description |
|---|---|---|---|
| `page` | integer | `0` | Zero-based page number |
| `size` | integer | `20` | Page size, capped at `100` |
| `sort` | string | `id` | Sort field (`id`, `microchipNumber`, `name`, `species`, or `breed`); optionally use `field,direction`, e.g. `name,asc` |
| `direction` | string | `asc` | Sort direction (`asc` or `desc`) when not included in `sort` |
| `species` | string | none | Optional case-insensitive exact species filter |

Example: `GET /api/pets?page=0&size=5&sort=name,asc`

```json
{
  "content": [
    {
      "id": 1,
      "microchipNumber": "CHIP-001",
      "name": "Clover",
      "species": "Rabbit",
      "breed": "Holland Lop"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "size": 5,
  "number": 0
}
```

Responses: `200 OK`; `400 Bad Request` for invalid page, size, sort, direction, or parameter type; `500 Internal Server Error` for unexpected errors.

### Get a pet

`GET /api/pets/{id}`

Returns the pet with the specified numeric ID. No request body.

Example response:

```json
{
  "id": 1,
  "microchipNumber": "CHIP-001",
  "name": "Clover",
  "species": "Rabbit",
  "breed": "Holland Lop"
}
```

Responses: `200 OK`; `404 Not Found` when the ID does not exist; `400 Bad Request` for an invalid ID; `500 Internal Server Error` for unexpected errors.

### Create a pet

`POST /api/pets`

Creates a pet and returns its resource URI in the `Location` header.

Request body (`Content-Type: application/json`):

| Field | Type | Required | Constraints |
|---|---|---|---|
| `microchipNumber` | string | Yes | Not blank; maximum 50 characters; unique |
| `name` | string | Yes | Not blank; maximum 100 characters |
| `species` | string | Yes | Not blank; maximum 80 characters |
| `breed` | string | Yes | Not blank; maximum 100 characters |

Example:

```json
{
  "microchipNumber": "CHIP-001",
  "name": "Clover",
  "species": "Rabbit",
  "breed": "Holland Lop"
}
```

Responses: `201 Created` with a `Location` header and `PetResponse` body; `400 Bad Request` with field validation details for invalid input; `409 Conflict` if the microchip number is already in use; `500 Internal Server Error` for unexpected errors.

### Replace a pet

`PUT /api/pets/{id}`

Replaces all editable pet fields; all four fields must be provided.

Request body uses the same fields and length limits as `POST`. Example:

```json
{
  "microchipNumber": "CHIP-001",
  "name": "Clover Updated",
  "species": "Rabbit",
  "breed": "Mini Lop"
}
```

Responses: `200 OK` with the updated `PetResponse`; `400 Bad Request` for missing, blank, oversized, or invalid fields; `404 Not Found` when the ID does not exist; `409 Conflict` if the microchip number belongs to another pet; `500 Internal Server Error` for unexpected errors.

### Partially update a pet

`PATCH /api/pets/{id}`

Updates only the supplied fields; omitted or `null` fields remain unchanged.

Request body: any subset of `microchipNumber`, `name`, `species`, and `breed`. Supplied strings must not be blank and must meet the same length limits as `POST`.

Example:

```json
{ "name": "Clover Patched" }
```

Responses: `200 OK` with the updated `PetResponse`; `400 Bad Request` for invalid fields; `404 Not Found` when the ID does not exist; `409 Conflict` if the microchip number belongs to another pet; `500 Internal Server Error` for unexpected errors.

### Delete a pet

`DELETE /api/pets/{id}`

Deletes the pet with the specified numeric ID. No request body.

Responses: `204 No Content` when deleted; `404 Not Found` when the ID does not exist; `400 Bad Request` for an invalid ID; `500 Internal Server Error` for unexpected errors.

## Validation and CORS

Request DTOs use Bean Validation and controller request bodies are validated with `@Valid`. The global exception handler returns the same `ErrorResponse` structure for validation, not-found, conflict, malformed-body, and unexpected errors.

CORS applies to `/api/**`. It allows origin `http://localhost:3000`, credentials, all request headers, and `GET`, `HEAD`, `POST`, `PUT`, `PATCH`, `DELETE`, and `OPTIONS`.

```powershell
curl.exe -i -X OPTIONS http://localhost:8080/api/pets -H "Origin: http://localhost:3000" -H "Access-Control-Request-Method: POST"
```

The response includes `Access-Control-Allow-Origin: http://localhost:3000`.
