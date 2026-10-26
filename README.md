# Bag Shop Management

Full-stack bag e-commerce demo: HTML/CSS/JS frontend + Spring Boot REST API.

## Run

```powershell
cd backend
mvn spring-boot:run
```

Open **http://localhost:8080/** (serves `frontend/` and `/api/*`).

Default database is in-memory **H2** with **500 seeded products** on first start.

## Demo account

Register any user on `/register.html`, then shop, cart, checkout, and orders.

## Push to GitHub

From this folder (after [Git](https://git-scm.com/) and [GitHub CLI](https://cli.github.com/) are installed):

```powershell
gh auth login
gh repo create bag-shop-management --public --source=. --remote=origin --push
```

If the repo name is taken, pick another name in `gh repo create`.
