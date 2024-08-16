StandIn ReadMe while this gets up and running

---

For development purposes to run the full project:

```shell
ENVIRONMENT=default docker compose up -d
```

---

To run the database in a container and run the spring stack locally for development:

```shell
ENVIRONMENT=local docker compose f docker-compose-local.yml up -d
```

Make sure your spring profile is also set to local (spring.profiles.active)