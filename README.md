<h1 style="font-size:32px; font-weight:700; color:#2c3e50; margin-bottom:10px;">
  API Test Utility Framework
</h1>

<h2 style="font-size:24px; font-weight:600; color:#34495e; margin-top:25px;">
  Overview
</h2>

<p style="font-size:16px; line-height:1.6; color:#555;">
planning for a clear architectural decision pays off for years. 
  three layers:
1. Base Framework → reusable engine

2. Template Project → skeleton for new API test projects

3. Implementation Projects → team‑specific tests

  This project is a lightweight Java API‑testing utility framework built on top of 
  <strong>RestAssured</strong> and <strong>JUnit 5</strong>. It provides reusable helpers for:
</p>

<ul style="font-size:16px; line-height:1.6; color:#555;">
  <li>Executing HTTP requests</li>
  <li>Manipulating URL query parameters</li>
  <li>Handling file operations for test data</li>
  <li>Running real integration tests against the Swagger Petstore API</li>
</ul>

<p style="font-size:16px; line-height:1.6; color:#555;">
  The goal is to keep API tests <strong>clean</strong>, <strong>readable</strong>, and 
  <strong>maintainable</strong> while avoiding repeated boilerplate code.
</p>

<hr style="margin:30px 0;">

<h2 style="font-size:24px; font-weight:600; color:#34495e;">
  Features
</h2>

<h3 style="font-size:20px; font-weight:600; color:#2c3e50; margin-top:20px;">
  1. RestAssuredHandler
</h3>

<p style="font-size:16px; line-height:1.6; color:#555;">
  A wrapper around RestAssured that standardizes:
</p>

<ul style="font-size:16px; line-height:1.6; color:#555;">
  <li>GET, POST, PUT, DELETE requests</li>
  <li>JSON content type</li>
  <li>Request/response logging</li>
  <li>Capturing status code, status description, and headers</li>
  <li>Returning the response body as a string</li>
</ul>

<p style="font-size:16px; line-height:1.6; color:#555;">
  This ensures every test uses the same request format and response‑handling logic.
</p>

<h3 style="font-size:20px; font-weight:600; color:#2c3e50; margin-top:20px;">
  2. QueryParametersHandler
</h3>

<p style="font-size:16px; line-height:1.6; color:#555;">
  Utility for manipulating URL query strings:
</p>

<ul style="font-size:16px; line-height:1.6; color:#555;">
  <li>Add parameters to a URL</li>
  <li>Remove parameters from a URL</li>
  <li>Parse and rebuild query strings while preserving order</li>
</ul>

<p style="font-size:16px; line-height:1.6; color:#555;">
  Useful for dynamically constructing API endpoints during tests.
</p>

<h3 style="font-size:20px; font-weight:600; color:#2c3e50; margin-top:20px;">
  3. FileHandler
</h3>

<p style="font-size:16px; line-height:1.6; color:#555;">
  Utility for:
</p>

<ul style="font-size:16px; line-height:1.6; color:#555;">
  <li>Saving files</li>
  <li>Reading files</li>
  <li>Checking file existence</li>
  <li>Downloading files</li>
  <li>Deleting files</li>
</ul>

<p style="font-size:16px; line-height:1.6; color:#555;">
  Used by tests that require temporary files or downloaded API responses.
</p>
