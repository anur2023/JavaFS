# ÉLUME — Fashion E-Commerce Frontend

React frontend for the Fashion E-Commerce with Virtual Try-On platform.

## Quick Start

### 1. Install dependencies
```bash
cd fashion-frontend
npm install
```

### 2. Run (backend must be on port 5000)
```bash
npm run dev
```

Open http://localhost:3000

---

## Pages & Routes

### Customer (role: CUSTOMER)
| Route | Page |
|---|---|
| `/customer` | Home dashboard |
| `/customer/products` | Browse & search products, add to wishlist, AR try-on |
| `/customer/wishlist` | View & manage saved items |
| `/customer/orders` | Place orders, view history, cancel, reorder |
| `/customer/tryon` | Virtual try-on history |
| `/customer/lookbook` | Browse & save seasonal lookbooks |
| `/customer/quiz` | Take style quiz, view responses |
| `/customer/inspiration` | Fashion inspiration feed |

### Admin (role: ADMIN)
| Route | Page |
|---|---|
| `/admin` | Dashboard with stats |
| `/admin/products` | Full product CRUD |
| `/admin/orders` | View all orders, update status & tracking |
| `/admin/lookbooks` | Create/edit lookbooks, manage items, publish/archive |
| `/admin/inspiration` | Post & manage inspiration content |
| `/admin/quiz` | Add & delete style quiz questions |

---

## How Auth Works
- Login → JWT stored in `localStorage` (`token`, `email`, `role`)
- Axios interceptor attaches `Authorization: Bearer <token>` to every request
- 401 responses auto-redirect to `/login`
- Role-based routing: CUSTOMER → `/customer/*`, ADMIN → `/admin/*`

## Proxy
Vite proxies `/api/*` → `http://localhost:5000/api/*` so no CORS issues in dev.

## Build for Production
```bash
npm run build
```
Outputs to `dist/`. Serve with nginx or any static server.
Point `/api` to your backend URL in nginx config.
