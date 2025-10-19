# 💎 StockControl Jewelry

**StockControl Jewelry** is an intelligent and modular system for **jewelry inventory management**, designed for **jewelry stores and individual jewelers**.  
It allows complete control over **jewels, metals, and stones**, recording **sales**, managing **restocking**, and handling multiple inventories and users from a single platform.  

Future versions will also include **AI-powered tools** for automatic jewel classification by image and smart material detection.

---

## 🧭 Overview

StockControl Jewelry aims to modernize jewelry inventory management by combining:
- Centralized control of materials and finished products.  
- Real-time collaboration between users.  
- Full traceability of every movement, sale, or restock.  
- Smart automation through machine learning.

---

## ⚙️ Core Features

### Jewelry Management
- Create and manage jewels with category, subcategory, metal, and stone composition.  
- Track stock, size, weight, and custom attributes.  
- Automatically handle restock requests for low-stock items.  

### Materials (Metals & Stones)
- Global materials available system-wide.  
- Custom materials per user or inventory.  
- Independent quantity tracking for each inventory.  

### Multi-Inventory System
- Each user can own multiple inventories (e.g., different stores).  
- Inventories can be **shared** among users.  
- Access permissions:
  - **Owner:** full control.  
  - **Write:** modify and update inventory data.  
  - **Read:** view-only access.  
- Automatic material synchronization when new inventories or jewels are created.  

### Multiuser Collaboration
- User authentication and registration.  
- Invitation-based collaboration with access levels.  
- Shared inventory support with granular permissions.  

### Sales & Restock System
- Persistent record of every sale, restock, or material change.  
- Pending restock module for automatic replenishment tracking.  
- Sale processing that updates jewel and material quantities.  

### Artificial Intelligence (in progress)
- Automatic jewel image classification (type, category, metal).  
- Integration with an external **Python FastAPI** microservice.  
- Smart suggestions during jewel creation.  

---

## 🧩 System Architecture

| Layer | Technology |
|-------|-------------|
| **Backend** | Java + Spring Boot |
| **Database** | JPA / Hibernate / MySQL |
| **Frontend** | React (planned) |
| **AI Service** | Python + FastAPI |
| **Cloud Storage** | Image hosting for jewel photos |

---

## 🧑‍💻 Author

Developed by [**@gerop7**](https://github.com/gerop7)  
> A personal and evolving project focused on improving jewelry management through clean architecture, automation, and AI.

---

 License

**© 2025 Gerop7. All Rights Reserved.**

This repository and its contents are protected under copyright law.  
You **may not** copy, modify, distribute, or use any part of this code or documentation without explicit written permission from the author.

For inquiries or collaborations, please contact **@gerop7** via GitHub.
