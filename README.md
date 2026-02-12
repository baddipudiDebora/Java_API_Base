**#API Test Utility Framework**
**##Overview**
This project is a lightweight Java API‑testing utility framework built on top of RestAssured and JUnit 5.
It provides reusable helpers for:

Executing HTTP requests

Manipulating URL query parameters

Handling file operations for test data

Running real integration tests against the Swagger Petstore API

The goal is to keep API tests clean, readable, and maintainable while avoiding repeated boilerplate code.

**##Features**
**###1. RestAssuredHandler**
A wrapper around RestAssured that standardizes:

GET, POST, PUT, DELETE requests

JSON content type

Logging

Capturing status code, status description, and headers

Returning the response body as a string

This ensures every test uses the same request format and response‑handling logic.

**###2. QueryParametersHandler**
Utility for manipulating URL query strings:

Add parameters to a URL

Remove parameters from a URL

Parse and rebuild query strings while preserving order

Useful for dynamically constructing API endpoints during tests.

**###. FileHandler**
Utility for:

Saving files

Reading files

Checking file existence

Downloading files

Deleting files

Used by tests that require temporary files or downloaded API responses.
