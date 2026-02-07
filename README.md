# 📚 Library Management Backend API

## 📖 Project Description

This project is a **backend-only API system** built using **Core PHP and MySQL**.

The system is designed with **role-based access control** and manages records through an **event-style workflow**, adapted for a **library management API structure**.

> ⚠️ This project has **NO frontend (UI)**
> All interactions are performed via **browser-based API endpoints** using GET requests.

---

## 👥 System Roles

### 🔹 Organizer

* Register & Login
* Create records (events)
* Update records
* Delete records
* View participants
* Remove participants

### 🔹 Member

* Register & Login
* View available records
* Join records
* View joined records

---

## 🛠 Technologies Used

* PHP (Core PHP)
* MySQL
* XAMPP (Apache & MySQL)
* Browser (for API testing)

---

## 🗄 Database Information

**Database Name:**

```
library_management_db
```

### 📑 Tables

1. **users**

   * Stores user accounts
   * Roles: `organizer`, `member`

2. **events**

   * Stores records (library items / events)
   * Linked to organizers

3. **event_participants**

   * Tracks member participation

✔ All tables are connected using **foreign keys**

---

## 📁 Project Folder Structure

```
library_management_api/
│
├── engine/
│   └── db.engine.php
│
├── services/
│   ├── account.service.php
│   └── event.service.php
│
├── http/
│   ├── account.http.php
│   └── event.http.php
│
├── engine.sql
├── index.php
└── README.md
```

---

## ▶️ How to Run the Project

1. Copy the project folder to:

```
C:\xampp\htdocs\library_management_api
```

2. Start **Apache** and **MySQL** from XAMPP Control Panel

3. Create database and tables:

   * Open phpMyAdmin
   * Import `engine.sql`

4. Open browser and test API:

```
http://localhost/library_management_api/index.php
```

---

## 🔐 API ENDPOINTS (FULL LIST)

All endpoints are accessed via:

```
index.php?module=MODULE_NAME&action=ACTION_NAME
```

---

## 👤 USER AUTHENTICATION ENDPOINTS

### 🔹 Register Organizer

```
index.php?module=account&action=register
&name=OrganizerOne
&email=org@mail.com
&password=123
&role=organizer
```

### 🔹 Register Member

```
index.php?module=account&action=register
&name=MemberOne
&email=mem@mail.com
&password=123
&role=member
```

### 🔹 Login (All Roles)

```
index.php?module=account&action=login
&email=org@mail.com
&password=123
```

### 🔹 Logout

```
index.php?module=account&action=logout
```

---

## 📘 RECORD / EVENT MANAGEMENT (CRUD)

### 🔹 Create Record (Organizer Only)

```
index.php?module=event&action=create
&title=Community Meetup
&location=Dar%20es%20Salaam
&date=2026-02-01
```

✔ Creates new record
✔ Links it to organizer

---

### 🔹 List Records (All Users)

```
index.php?module=event&action=list
```

✔ Displays all records
✔ Includes organizer name

---

### 🔹 Update Record (Organizer Only)

```
index.php?module=event&action=update
&event_id=1
&title=Tech Meetup
&location=Arusha
&date=2026-02-10
```

✔ Updates record details

---

### 🔹 Delete Record (Organizer Only)

```
index.php?module=event&action=delete
&event_id=1
```

✔ Deletes record

---

## 👥 PARTICIPATION MANAGEMENT (CRUD)

### 🔹 Join Record (Member Only)

```
index.php?module=event&action=join
&event_id=1
```

✔ Member joins record

---

### 🔹 View Participants (All Users)

```
index.php?module=event&action=participants
```

✔ Lists all participants with joined time

---

### 🔹 Remove Participant (Organizer Only)

```
index.php?module=event&action=remove-participant
&event_id=1
&member_id=2
```

✔ Removes member from record

---

## 🔒 Security Features

* Password hashing using `password_hash()`
* Session-based authentication
* Role-based authorization
* Unauthorized access blocked
* Protected routes using `auth()`

---

## ✅ CRUD Summary

| Feature          | Create | Read | Update | Delete |
| ---------------- | ------ | ---- | ------ | ------ |
| Users            | ✔      | ✔    | ❌      | ❌      |
| Records (Events) | ✔      | ✔    | ✔      | ✔      |
| Participation    | ✔      | ✔    | ❌      | ✔      |

