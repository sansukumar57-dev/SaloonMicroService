# AGENTS — quick reference for AI coding agents

Purpose: give an AI (or developer) the minimal, actionable knowledge to be productive in this microservice monorepo.

Top-level architecture (big picture)
- Type: Spring Boot microservices (Java 17, Spring Boot 3.x, Spring Cloud Netflix Eureka + Gateway).
- Major components (see service folders): `eureka-server/`, `gateway-server/`, `booking-service/`, `payment-service/`, `salonService/`, `catagory_microservice/`, `service-offering/`, plus `service/` and others. Each is a standalone Spring Boot app with its own `pom.xml` and `src/`.
- Service discovery: Eureka (app `eureka-server`), configured at `eureka-server/src/main/resources/application.yml` (port 8070). Services register with Eureka using `spring-cloud-starter-netflix-eureka-client` (example: `booking-service/pom.xml`).
- Edge routing: Spring Cloud Gateway (app `gateway-server`) — routes are declared in `gateway-server/src/main/resources/application.yml` and use logical service IDs with the `lb://` scheme (e.g. `lb://SALON-SERVICE`). Gateway listens on port 5000 by default.
- Datastores: Each service typically uses its own MySQL schema (see `docker-compose/default/docker-compose.yaml` and each service `application.yml` with `spring.datasource.url` like `jdbc:mysql://localhost:3306/booking_db`).
- Containerization: Many modules include the `jib-maven-plugin` with image names like `dailypurpose/<service>:v1` (see `booking-service/pom.xml`), and there is a `docker-compose/default/docker-compose.yaml` that assembles DBs, Eureka and prebuilt images for local full-stack runs.

Critical developer workflows
- Build one module (Windows):
  - Open a terminal in the module directory and run: `.\mvnw.cmd clean package -DskipTests`.
  - The produced jar is in `target/` (e.g. `booking-service/target/booking-service-0.0.1-SNAPSHOT.jar`).
- Build and publish Docker image (via Jib):
  - `.\mvnw.cmd jib:build` (POM contains `to.image` in some modules). Note: jib pushes to a registry unless configured for `dockerBuild`; check `pom.xml` for plugin configuration.
  - For local image without pushing: `.\mvnw.cmd com.google.cloud.tools:jib-maven-plugin:3.5.1:dockerBuild` or configure jib to use the `dockerBuild` goal if you prefer a local image.
- Full local stack with docker-compose (recommended for integration testing):
  - From repo root: `docker compose -f docker-compose/default/docker-compose.yaml up --build` (or `docker-compose` if your environment uses the legacy binary).
  - The compose file exposes DB ports 3301..3307 and service ports 5000..5006. Eureka is on 8070.
  - Health checks use Actuator endpoints (e.g. `/actuator/health`) — services here expose them and compose depends on them.
- Run an individual service from IDE: create a run configuration pointing to the service main class (e.g. `com.xyz.booking_service.BookingServiceApplication`) and set active profiles/environment variables to match `application.yml` (notably `SPRING_DATASOURCE_URL` if you want to point at dockerized DBs).

Project-specific conventions & patterns
- Naming / Eureka registration:
  - `spring.application.name` is set in each service `application.yml` (e.g. `booking-service`). Gateway routes, however, use `lb://<UPPERCASE-NAME>` (e.g. `lb://BOOKING-SERVICE` or `lb://SALON-SERVICE`) — Spring Cloud/Eureka canonicalizes service IDs to uppercase in route definitions; when matching, use the same service id pattern found in `gateway-server/src/main/resources/application.yml`.
- Ports are explicit in each service's `application.yml`. Examples:
  - `gateway-server` — port 5000 (file: `gateway-server/src/main/resources/application.yml` lines 1-3)
  - `booking-service` — port 5005 (`booking-service/src/main/resources/application.yml` line 16)
  - `eureka-server` — port 8070 (`eureka-server/src/main/resources/application.yml` line 6)
- Jib-based images: many `pom.xml` files declare `jib-maven-plugin` with `to.image` pre-filled to `dailypurpose/<service>:v1`. The docker-compose references those images. If running locally, either build images locally with jib dockerBuild or override compose to use local build contexts.
- Health endpoints: docker-compose expects `/actuator/health` to be available for containers' readiness checks — prefer using Actuator when adding endpoints or health indicators.

Integration points & external dependencies
- Eureka server: `http://localhost:8070/eureka/` — verify `application.yml` files that set `eureka.client.service-url.defaultZone` or environment variable `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` used in docker-compose.
- MySQL instances used per service: docker-compose provides separate MySQL containers (userdb, bookingdb, categorydb, paymentdb, salondb, serviceofferingdb) mapped to host ports 3301..3307. When running services directly from the IDE, point `spring.datasource.url` to either the host DB (jdbc:mysql://localhost:3302 etc) or to a local dev DB.
- (Optional) Auth: `gateway-server` contains OAuth2 JWT issuer config (issuer-uri: `http://localhost:8080/realms/master`) — indicates Keycloak or similar may be used in some deployments; auth config is present but can be commented out in examples.

Where to look first (key files)
- Service entry points: `**/*/src/main/java/**/*Application.java` (examples: `eureka-server/src/main/java/.../EurekaServerApplication.java`).
- Service configs: `**/*/src/main/resources/application.yml` (gateway, eureka, booking, payment, etc). Gateway routes are in `gateway-server/src/main/resources/application.yml`.
- Build / image: `**/*/pom.xml` for Java version (Java 17), Spring Boot / Cloud versions and `jib-maven-plugin` entries.
- Full-stack launcher: `docker-compose/default/docker-compose.yaml` — includes DBs, Eureka and image names used in this repo.

Agent tasks the repo normally expects
- Read gateway routes when making API changes: add new service -> add mapping in `gateway-server/src/main/resources/application.yml` using the repo's `lb://` pattern.
- When changing DB schema or JPA entities: update `application.yml`'s `spring.jpa.hibernate.ddl-auto` and verify MySQL container mapping in `docker-compose`.
- Building images for compose: either rely on the published `dailypurpose/*:v1` images or run jib locally and update compose image references.

Quick examples (copyable)
- Build and run booking-service locally (Windows PowerShell):
  - cd booking-service; .\mvnw.cmd clean package -DskipTests; java -jar target\booking-service-0.0.1-SNAPSHOT.jar
- Start full stack with docker compose (PowerShell):
  - docker compose -f docker-compose/default/docker-compose.yaml up --build
- Rebuild gateway image locally via Jib docker build (Windows PowerShell):
  - cd gateway-server; .\mvnw.cmd com.google.cloud.tools:jib-maven-plugin:3.5.1:dockerBuild

Notes / gotchas
- Use the module's `mvnw` wrapper script for consistent Maven + Java versions. On Windows use `mvnw.cmd`.
- Many examples in compose assume images named `dailypurpose/<service>:v1` are available. If you don't have those on Docker Hub, either build locally (jib/dockerBuild) or modify compose to use local build contexts.
- Service names in gateway route definitions are uppercase in the config; when adding new services check both `spring.application.name` and the gateway's route target.

If the agent needs more detail, useful follow-ups:
- Produce a map of service-to-port and service-to-database mappings (can be auto-generated by parsing the `application.yml` files).
- Generate per-module run/debug instructions (IDE runner configs) for the most commonly edited services (`gateway-server`, `eureka-server`, `booking-service`).

---
Generated by an automated codebase scan. If you want, I can now produce the service-to-port map or create sample run configurations for your IDE.
