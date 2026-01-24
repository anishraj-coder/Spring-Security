# Auth App Frontend: Modern Secure Authentication Client

![Auth App Banner](image.png)  
*Placeholder for project banner or hero screenshot (e.g., login page or dashboard overview). Replace with your actual image.*

**Auth App Frontend** is a professional-grade, security-first React application built with modern tools and best practices. It serves as the client for a stateless authentication system, integrating seamlessly with a Spring Boot 3+ backend to deliver local email/password authentication, Google & GitHub OAuth2, role-based access control (RBAC), automated token rotation, and a premium user experience.

This repository contains **only the frontend implementation**. It emphasizes secure token handling (no localStorage for JWTs), in-memory state management, silent refresh flows, and clean, maintainable code architecture.

> **Version**: 1.0.0  
> **Author**: Anish Raj  
> **Last Updated**: January 2026  
> **License**: MIT (educational & template use encouraged)

---

## Table of Contents

- [Project Overview](#project-overview)
- [Motivation & Design Goals](#motivation--design-goals)
- [Key Features](#key-features)
- [Tech Stack & Dependencies](#tech-stack--dependencies)
- [System Architecture](#system-architecture)
  - [Folder Structure](#folder-structure)
  - [Stateless JWT Silent Refresh Flow](#stateless-jwt-silent-refresh-flow)
  - [Axios Interceptors & Auto-Retry](#axios-interceptors--auto-retry)
  - [State Management Strategy](#state-management-strategy)
- [Security Implementation](#security-implementation)
  - [Role-Based Access Control (RBAC)](#role-based-access-control-rbac)
  - [Route Guarding & UI Gating](#route-guarding--ui-gating)
  - [OAuth2 Redirect Handling](#oauth2-redirect-handling)
- [Feature Deep-Dive](#feature-deep-dive)
  - [Authentication Pages](#authentication-pages)
  - [User Dashboard & Profile](#user-dashboard--profile)
  - [Admin Suite](#admin-suite)
  - [Toast Notifications & Error Handling](#toast-notifications--error-handling)
- [API Integration](#api-integration)
  - [Base URL Configuration](#base-url-configuration)
  - [Key Endpoints Used](#key-endpoints-used)
- [Setup & Installation](#setup--installation)
  - [Prerequisites](#prerequisites)
  - [Environment Variables](#environment-variables)
  - [Running the Application](#running-the-application)
- [Development Practices](#development-practices)
  - [Clean Code Principles](#clean-code-principles)
  - [Type Safety](#type-safety)
  - [Custom Hooks](#custom-hooks)
- [Screenshots (Placeholders)](#screenshots-placeholders)
- [Troubleshooting Common Issues](#troubleshooting-common-issues)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)
- [Acknowledgments](#acknowledgments)

---

## Project Overview

Auth App Frontend demonstrates industry-standard practices for building secure, scalable React applications with authentication. It deliberately avoids common security pitfalls (e.g., storing JWTs in localStorage) and instead implements a robust dual-token flow using HttpOnly refresh cookies from the backend combined with in-memory access token storage.

The application provides:
- Seamless local and social authentication
- Protected routes with role-based guards
- Admin user management interface
- Modern, responsive UI with dark mode support
- Silent session persistence across page refreshes

It pairs perfectly with the Securify Spring Boot backend for a complete full-stack authentication solution.

---

## Motivation & Design Goals

This frontend was built to:
1. Showcase secure JWT handling in React without exposing tokens to XSS attacks.
2. Demonstrate proper integration of modern tools (Zustand, React Query, Shadcn/UI).
3. Implement clean architecture with feature-based organization.
4. Provide a production-ready template for authentication flows.
5. Prioritize developer experience and type safety with TypeScript.

Core principles:
- Security first
- Separation of concerns
- Declarative and readable code
- Optimal performance with React Query caching

---

## Key Features

- Stateless dual-token authentication (access token in memory, refresh token in HttpOnly cookie)
- Multi-provider login (Local, Google, GitHub)
- Automated email verification for local accounts
- Silent token refresh on app mount and 401 errors
- Role-based route protection (USER vs ADMIN)
- Admin dashboard with user listing, pagination, verification, and deletion
- User profile editing with real-time UI updates
- Responsive, modern UI with Tailwind + Shadcn/UI
- Toast notifications for success/error states
- Smooth animations via Framer Motion

---

## Tech Stack & Dependencies

| Category              | Technology                               | Purpose                                      |
|-----------------------|------------------------------------------|----------------------------------------------|
| Framework             | React 19 + TypeScript                    | Core UI & type safety                        |
| Build Tool            | Vite                                     | Fast development & build                     |
| Routing               | React Router v7                          | Declarative navigation & guarded routes      |
| Styling               | Tailwind CSS + Shadcn/UI                 | Utility-first styling & accessible components|
| Icons                 | Lucide React                             | Consistent icon set                          |
| State Management      | Zustand                                  | Lightweight in-memory global store           |
| Data Fetching         | Tanstack React Query                     | Server state, caching, auto-refetch           |
| HTTP Client           | Axios                                    | Requests with interceptors                   |
| Forms                 | React Hook Form + Zod                    | Performant forms & validation                |
| Notifications         | Sonner (or React Hot Toast)              | Beautiful toast messages                     |
| Animations            | Framer Motion                            | Smooth transitions & micro-interactions       |

Key `package.json` dependencies (excerpt):

    "@tanstack/react-query": "^5.0.0",
    "axios": "^1.7.0",
    "react-hook-form": "^7.0.0",
    "zod": "^3.0.0",
    "zustand": "^4.0.0",
    "@shadcn/ui": "latest",
    "tailwindcss": "^3.0.0",
    "framer-motion": "^11.0.0",
    "lucide-react": "^0.0.0"

---

## System Architecture

### Folder Structure

The project uses a **feature-based** structure for scalability:

    src/
    ├── api/                  # Axios instance, interceptors, base config
    │   └── client.ts
    ├── components/           # Reusable UI components (atomic design)
    │   ├── auth/             # RoleGuard, AuthLayout, etc.
    │   ├── ui/               # Shadcn extended components (Button, Card, etc.)
    │   ├── layout/           # Header, Footer, Sidebar
    │   └── shared/           # Toast, Loader, Avatar
    ├── hooks/                # Custom hooks (business logic)
    │   ├── useAuth.ts        # Login, logout, refresh logic
    │   ├── useUser.ts        # Profile fetch & update
    │   └── useAdmin.ts       # Admin actions (list, verify, delete)
    ├── pages/                # Page components
    │   ├── Home.tsx
    │   ├── Login.tsx
    │   ├── Register.tsx
    │   ├── Dashboard/
    │   └── Admin/
    ├── routes/               # Router configuration & guards
    │   ├── ProtectedRoute.tsx
    │   └── router.tsx
    ├── store/                # Zustand stores
    │   └── authStore.ts      # User & token state
    ├── types/                # TypeScript interfaces (User, API responses)
    ├── utils/                # Helpers, constants, validators
    └── App.tsx               # Root component with QueryClientProvider

### Stateless JWT Silent Refresh Flow

1. **App Mount** → useEffect in App.tsx calls `/auth/refresh`
2. Browser sends HttpOnly refresh cookie automatically
3. Backend validates & returns new access token + new refresh cookie
4. Access token stored in Zustand (in-memory only)
5. All API calls attach `Authorization: Bearer <token>` via Axios interceptor
6. On 401 → Response interceptor triggers refresh → retries original request

This ensures sessions survive page refreshes without token exposure.

### Axios Interceptors & Auto-Retry

- Request interceptor: Adds Bearer token from Zustand
- Response interceptor:
  - Catches 401
  - Calls refresh endpoint
  - Updates Zustand token
  - Retries failed request (once)

### State Management Strategy

- **Zustand**: Authentication state (user object, access token, isAuthenticated)
- **React Query**: Server data (user list, profile) with caching and invalidation

---

## Security Implementation

### Role-Based Access Control (RBAC)

User object contains `roles: ['USER' | 'ADMIN']` array.

### Route Guarding & UI Gating

- `<ProtectedRoute>`: Redirects unauthenticated users to login
- `<RoleGuard allowedRoles={['ADMIN']}>`: Children only render if role matches
- Header dynamically shows/hides links based on roles

### OAuth2 Redirect Handling

Dedicated `/oauth2/redirect` page:
- Extracts access token from backend redirect URL
- Stores in Zustand
- Fetches user profile
- Redirects to dashboard on success, login on failure

---

## Feature Deep-Dive

### Authentication Pages

- Login & Register: Zod validation, React Hook Form, loading states
- Forgot/Reset Password: Full flow integration
- Email verification toast on registration

### User Dashboard & Profile

- Displays user stats
- Editable form with PATCH updates
- Avatar upload support
- Real-time Header sync after profile changes

### Admin Suite

- Paginated table (React Query + keepPreviousData)
- Actions: Verify user, change role, delete account
- Modal with detailed user info

### Toast Notifications & Error Handling

Sonner toasts for:
- Success (login, update)
- Error (validation, server errors)
- Info (verification email sent)

---

## API Integration

### Base URL Configuration

`.env` file:

    VITE_API_URL=http://localhost:8080/api

Used in axios baseURL.

### Key Endpoints Used

All calls go through custom hooks with React Query.

---

## Setup & Installation

### Prerequisites

- Node.js v20+
- pnpm v10+ (or npm/yarn)
- Running Securify Backend instance

### Environment Variables

Create `.env` in root:

    VITE_API_URL=http://localhost:8080/api

### Running the Application

    pnpm install
    pnpm dev

Opens at http://localhost:3000 
---

## Development Practices

### Clean Code Principles

- Components are UI-only
- Business logic in hooks
- Single source of truth (Zustand + React Query)
- Declarative security with RoleGuard

### Type Safety

Full TypeScript with interfaces for:
- User
- API responses/errors
- Form data

### Custom Hooks

Encapsulate all side effects and API calls for reusability.

---

## Screenshots (Placeholders)

### Login Page
![Login Page](https://via.placeholder.com/1200x800?text=Login+Page+Screenshot)

### Registration Page
![Registration Page](https://via.placeholder.com/1200x800?text=Registration+Page+Screenshot)

### User Dashboard
![User Dashboard](https://via.placeholder.com/1200x800?text=User+Dashboard+Screenshot)

### Admin Panel
![Admin Panel](https://via.placeholder.com/1200x800?text=Admin+User+Management+Screenshot)

### Profile Edit Modal
![Profile Edit](https://via.placeholder.com/800x600?text=Profile+Edit+Modal)

---

## Troubleshooting Common Issues

- **No refresh on app load**: Check backend is running and refresh cookie exists
- **CORS errors**: Ensure backend allows frontend origin
- **OAuth redirect fails**: Verify redirect URI in backend env matches Vite dev server
- **Toast not showing**: Check Sonner provider in App.tsx

---

## Future Enhancements

- Dark mode toggle
- Password strength meter
- 2FA support
- Mobile-responsive improvements
- Testing suite (Vitest + React Testing Library)
- Deployment config (Vercel/Netlify)

---

## Contributing

Fork and submit PRs! Focus on:
- Bug fixes
- New components
- Accessibility improvements

---

## Acknowledgments

Built with inspiration from modern auth templates, OWASP guidelines, and the Shadcn/UI ecosystem.

Thank you for using Auth App Frontend, Anish!

---