<!doctype html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>Java API Base — README</title>
  <style>
    body { font-family: "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; line-height:1.6; color:#222; padding:28px; max-width:980px; margin:auto; }
    h1 { color:#0b5fff; margin-bottom:6px; font-size:28px; }
    h2 { color:#0b5fff; margin-top:22px; font-size:20px; }
    h3 { color:#333; margin-top:18px; font-size:16px; }
    p { margin:10px 0; }
    pre { background:#f6f8fa; padding:12px; border-radius:6px; overflow:auto; }
    code { background:#f1f1f1; padding:2px 6px; border-radius:4px; }
    table { border-collapse:collapse; width:100%; margin-top:12px; }
    th, td { border:1px solid #e1e4e8; padding:8px 10px; text-align:left; vertical-align:top; }
    th { background:#f6f8fa; font-weight:700; }
    .muted { color:#666; font-size:0.95em; }
    .pill { display:inline-block; background:#eef6ff; color:#0b5fff; padding:4px 8px; border-radius:999px; font-weight:600; margin-right:6px; }
    a { color:#0b5fff; text-decoration:none; }
    a:hover { text-decoration:underline; }
    ul { margin:8px 0 12px 20px; }
    hr { border:0; border-top:1px solid #e6e6e6; margin:20px 0; }
    .small { font-size:0.95em; color:#444; }
  </style>
</head>
<body>

<h1>Java API Base</h1>
<p class="muted">Lightweight, reusable Java framework for API test automation. Provides standardized utilities for HTTP requests, query parameter handling, JSON processing, file management, reporting, and secure authentication.</p>

<hr/>

<h2>Overview</h2>
<p><strong>API Test Utility Framework</strong></p>
<p>Planning for a clear architectural decision pays off for years. This project follows a three‑layer model:</p>
<ul>
  <li><strong>Base Framework</strong> — reusable engine (this repository)</li>
  <li><strong>Template Project</strong> — skeleton for new API test projects (archetype)</li>
  <li><strong>Implementation Projects</strong> — team‑specific tests that depend on the template</li>
</ul>
<p>This framework is built on <strong>RestAssured</strong> and <strong>JUnit 5</strong> and provides helpers for executing HTTP requests, manipulating query parameters, handling test data files, and publishing test reports. The goal is clean, readable, maintainable API tests without repeated boilerplate.</p>

<h2>What’s included</h2>
<ul>
  <li><strong>RestAssuredHandler</strong> — centralized HTTP client wrapper for consistent request/response handling and logging.</li>
  <li><strong>QueryParametersHandler</strong> — URL and query string manipulation utilities.</li>
  <li><strong>JsonUtility</strong> — JSON parsing and serialization helpers.</li>
  <li><strong>FileHandler</strong> — file I/O, artifact saving, and test data management.</li>
  <li><strong>ExtentReportHandler</strong> &amp; <strong>Reporter</strong> — ExtentReports integration and structured report publishing.</li>
  <li><strong>Logger</strong> — centralized logging used across handlers.</li>
  <li><strong>Auth helper</strong> — AWS Secrets Manager integration and token generation for local/CI usage.</li>
  <li><strong>Unit tests</strong> — utilities covered by unit tests to ensure framework quality and maintainability.</li>
</ul>

<h2>Quick start</h2>
<pre><code>mvn clean install
mvn test</code></pre>

<p class="small">Example usage (conceptual):</p>
<pre><code>String response = RestAssuredHandler.get("/api/pets/1");
FileHandler.saveFile("response.json", response);
Reporter.publish();</code></pre>

<h2>Configuration examples</h2>
<ul>
  <li><code>env.mode</code> = <code>local</code> | <code>ci</code> | <code>aws</code></li>
  <li><code>aws.region</code> = <code>us-east-1</code></li>
  <li><code>secrets.manager.arn</code> = ARN for Secrets Manager</li>
  <li><code>auth.clientId</code> = client id for token generation</li>
</ul>
<p class="muted">Document environment values in <code>ENVIRONMENT.md</code> (recommended).</p>

<h2>Architecture and project model</h2>
<p><strong>Three-layer model</strong></p>
<ol>
  <li><strong>Base Framework</strong> — reusable engine containing handlers, utilities, reporting, and auth helpers.</li>
  <li><strong>Template Project</strong> — starter skeleton (Maven archetype) that references the base and demonstrates BDD wiring and sample tests.</li>
  <li><strong>Implementation Projects</strong> — team-specific test suites that depend on the template and the base.</li>
</ol>
<p>This separation enforces <strong>reusability</strong>, <strong>consistency</strong>, and <strong>fast onboarding</strong> for new API test projects.</p>

<h2>Current ISTQB alignment and satisfied objectives</h2>
<p class="muted">Evaluated against CTAL‑TAE v2.0 and CT‑TAS v1.0.</p>

<table>
  <thead>
    <tr><th>Area</th><th>Implemented</th><th>Mapped ISTQB objectives</th></tr>
  </thead>
  <tbody>
    <tr><td>Architecture &amp; Layering</td><td>Layered handlers and modular packages</td><td>CTAL‑TAE 3.1.1; 3.1.3</td></tr>
    <tr><td>Design &amp; Maintainability</td><td>Facade/Adapter handlers with unit tests</td><td>CTAL‑TAE 3.1.5; 4.3.1</td></tr>
    <tr><td>Secure Credential Handling</td><td>Auth helper (Secrets Manager + token gen)</td><td>CT‑TAS TAS‑B03; CTAL‑TAE 2.1.2</td></tr>
    <tr><td>Reporting &amp; Evidence</td><td>ExtentReports + FileHandler for artifacts</td><td>CTAL‑TAE 6.1.1; CT‑TAS TAS‑B13</td></tr>
    <tr><td>CI Readiness</td><td>Maven structure and modular tests</td><td>CTAL‑TAE 5.1.1; CT‑TAS 3.2</td></tr>
  </tbody>
</table>

<h2>Already implemented improvements (mapped to ISTQB)</h2>
<ul>
  <li><strong>Unit tests for utilities</strong> — <em>CTAL‑TAE 7.1.4</em></li>
  <li><strong>Auth helper for secure secrets retrieval and token generation</strong> — <em>CT‑TAS TAS‑B03; CTAL‑TAE 2.1.2</em></li>
  <li><strong>ExtentReports integration and Reporter utilities</strong> — <em>CTAL‑TAE 6.1.1; CT‑TAS TAS‑B13</em></li>
  <li><strong>Modular handler design</strong> (RestAssured, JSON, query params, file I/O) — <em>CTAL‑TAE 3.1.3; 3.1.5</em></li>
  <li><strong>Maven archetype local starter template</strong> referencing this base — <em>CT‑TAS 4.1.1; CTAL‑TAE 3.1.2</em></li>
</ul>

<h2>In progress improvements (mapped to ISTQB)</h2>
<ul>
  <li><strong>ARCHITECTURE.md</strong> (diagram + class mapping) — <em>CTAL‑TAE 3.1.2</em></li>
  <li><strong>ENVIRONMENT.md</strong> &amp; <strong>application.properties.template</strong> — <em>CTAL‑TAE 2.1.1; CT‑TAS 4.3.2</em></li>
  <li><strong>CI workflows</strong> (GitHub Actions for unit &amp; integration tests, report publishing) — <em>CTAL‑TAE 5.1.1; CT‑TAS 6.1</em></li>
  <li><strong>Static analysis &amp; quality gates</strong> (Checkstyle/SpotBugs/PMD) — <em>CTAL‑TAE 7.1.4; 4.3</em></li>
  <li><strong>Metrics exporter</strong> (JSON) &amp; dashboard guidance — <em>CTAL‑TAE 6.1.2; CT‑TAS 5.2.1</em></li>
  <li><strong>Verification checklist &amp; flakiness detection</strong> — <em>CTAL‑TAE 7.1.1; 8.1.1</em></li>
  <li><strong>Pluggable interfaces</strong> for HttpClient/FileStore/AuthProvider — <em>CTAL‑TAE 3.1.3; CT‑TAS 3.1.2</em></li>
</ul>

<h2>Local starter boilerplate (Maven archetype)</h2>
<p class="pill">Archetype</p>
<p>A Maven archetype starter template is available to scaffold new API projects that reference this base. The archetype includes example BDD feature files and sample test wiring.</p>
<p><a href="https://github.com/baddipudiDebora/BDD-Java-API-Archtype-Project-Template/tree/main" target="_blank">BDD-Java-API-Archtype-Project-Template</a></p>

<h2>Roadmap &amp; priorities</h2>
<p><strong>Short term:</strong> ARCHITECTURE.md, ENVIRONMENT.md, application.properties.template, CI unit workflow.</p>
<p><strong>Medium term:</strong> Integration profile (Testcontainers/LocalStack), static analysis, metrics exporter.</p>
<p><strong>Long term:</strong> Flakiness detection, pluggable interfaces, compliance checklist.</p>

<hr/>
<p class="muted">Maintainer: Debora Baddipudi — <a href="https://github.com/baddipudiDebora/Java_API_Base">Java_API_Base</a></p>

</body>
</html>
