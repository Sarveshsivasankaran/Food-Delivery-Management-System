<body style="font-family: 'Segoe UI', Roboto, sans-serif; line-height:1.6; background:#0f1724; color:#e8f1ff; padding:40px; max-width:900px; margin:auto;">

  <h1 style="font-size:2em; color:#33AEFF;">🍴 Restaurant Management System</h1>
  <p>A full-featured <strong>JavaFX + MySQL</strong> desktop application designed to digitize local restaurant operations — including customer ordering, restaurant menu management, delivery tracking, and admin supervision — all in one platform.</p>

  <h2 style="color:#33AEFF;">📖 Overview</h2>
  <p>In our state, the food service industry plays a major role in daily life. Although there are large-scale online platforms for food ordering, many local restaurants still lack an efficient digital system to manage operations.</p>
  <p>This <strong>Restaurant Management System</strong> bridges that gap by offering a database-driven solution to streamline restaurant workflows and improve coordination between customers, restaurants, delivery agents, and administrators.</p>

  <h2 style="color:#33AEFF;">🎯 Project Objective</h2>
  <p>The main goal of this project is to simplify and automate the order and delivery process for local restaurants. The system provides a centralized application where:</p>
  <ul>
    <li>Customers can place and track orders easily.</li>
    <li>Restaurants can manage menus and monitor order progress.</li>
    <li>Delivery agents can update real-time delivery statuses.</li>
    <li>Admins can oversee the entire system for transparency and control.</li>
  </ul>

  <h2 style="color:#33AEFF;">⚙️ Tech Stack</h2>
  <table style="width:100%; border-collapse:collapse; color:#dbeafe;">
    <thead style="background:rgba(255,255,255,0.1);">
      <tr><th align="left">Component</th><th align="left">Technology</th></tr>
    </thead>
    <tbody>
      <tr><td>Frontend / UI</td><td>JavaFX</td></tr>
      <tr><td>Backend</td><td>Java</td></tr>
      <tr><td>Database</td><td>MySQL</td></tr>
      <tr><td>IDE (Recommended)</td><td>IntelliJ IDEA / Eclipse / NetBeans</td></tr>
      <tr><td>JDBC Driver</td><td>MySQL Connector/J</td></tr>
    </tbody>
  </table>

  <h2 style="color:#33AEFF;">🧩 System Architecture</h2>
  <p>The application is modularized into four main components:</p>

  <h3>👨‍🍳 1. Customer Module</h3>
  <ul>
    <li>Register and log in to the system.</li>
    <li>Browse menu items from different restaurants.</li>
    <li>Place food orders and view order history.</li>
    <li>Track order status in real-time.</li>
  </ul>

  <h3>🏪 2. Restaurant Module</h3>
  <ul>
    <li>Manage restaurant menus (add/update/delete dishes).</li>
    <li>View incoming customer orders and their statuses.</li>
    <li>Track ongoing and completed orders.</li>
  </ul>

  <h3>🚚 3. Delivery Agent Module</h3>
  <ul>
    <li>View assigned delivery orders.</li>
    <li>Update delivery status: <em>Pending → Preparing → Out for Delivery → Delivered</em>.</li>
    <li>Ensure real-time updates to customers.</li>
  </ul>

  <h3>🧑‍💼 4. Admin Module</h3>
  <ul>
    <li>Manage all users (add/delete accounts).</li>
    <li>View all orders, users, and menu items.</li>
    <li>Monitor complete system activity through a centralized dashboard.</li>
  </ul>

  <h2 style="color:#33AEFF;">🧠 Key Features</h2>
  <ul>
    <li>✅ <strong>Role-based Access Control</strong> — Customer, Restaurant, Delivery Agent, and Admin dashboards.</li>
    <li>✅ <strong>Secure Authentication</strong> — User registration and login using MySQL database.</li>
    <li>✅ <strong>Automated Order Assignment</strong> — Random delivery agent allocation for new orders.</li>
    <li>✅ <strong>Real-Time Order Tracking</strong> — Customers can monitor order progress.</li>
    <li>✅ <strong>Dynamic Menu Management</strong> — Restaurants can modify dishes and prices anytime.</li>
    <li>✅ <strong>Admin Oversight</strong> — Global visibility and user management from a single panel.</li>
    <li>✅ <strong>Stylish JavaFX UI</strong> — Gradient themes and clean, modern interface.</li>
  </ul>

  <h2 style="color:#33AEFF;">🗄 Database Schema</h2>
  <p><strong>Database Name:</strong> Restaurant</p>
  <table style="width:100%; border-collapse:collapse;">
    <thead style="background:rgba(255,255,255,0.1);">
      <tr><th align="left">Table</th><th align="left">Description</th></tr>
    </thead>
    <tbody>
      <tr><td>Users</td><td>Stores all user details with role (customer, restaurant, delivery, admin).</td></tr>
      <tr><td>Menu</td><td>Contains menu items, prices, and restaurant names.</td></tr>
      <tr><td>Orders</td><td>Records customer orders, assigned delivery agent, and order status.</td></tr>
    </tbody>
  </table>

  <h2 style="color:#33AEFF;">🧰 Setup Instructions</h2>
  <h3>🔧 Prerequisites</h3>
  <ul>
    <li>Java JDK 17+</li>
    <li>MySQL Server & MySQL Workbench</li>
    <li>MySQL Connector/J (JDBC Driver)</li>
    <li>JavaFX SDK (if not bundled with your IDE)</li>
  </ul>

  <h3>🚀 Steps to Run</h3>
  <ol>
    <li><strong>Clone the repository</strong></li>
  </ol>
  <pre><code>git clone https://github.com/&lt;your-username&gt;/Restaurant-Management-System.git
cd Restaurant-Management-System
  </code></pre>

  <ol start="2">
    <li><strong>Configure MySQL</strong></li>
  </ol>
  <ul>
    <li>Open MySQL Workbench.</li>
    <li>Create a database named <code>Restaurant</code> (or it will auto-create on first run).</li>
    <li>Update credentials in code:
      <pre><code>private static final String USER = "root";
private static final String PASSWORD = "";</code></pre>
    </li>
  </ul>

  <ol start="3">
    <li><strong>Open in IDE</strong></li>
  </ol>
  <ul>
    <li>Open the project in IntelliJ / Eclipse.</li>
    <li>Add JavaFX SDK and MySQL Connector/J to project libraries.</li>
  </ul>

  <ol start="4">
    <li><strong>Run the Application</strong></li>
  </ol>
  <ul>
    <li>Execute <code>Main.java</code>.</li>
    <li>Database and tables auto-create on first launch.</li>
    <li>Log in or register to explore each module.</li>
  </ul>

  <h2 style="color:#33AEFF;">🧪 Sample Credentials (for testing)</h2>
  <table style="width:100%; border-collapse:collapse;">
    <thead style="background:rgba(255,255,255,0.1);">
      <tr><th>Role</th><th>Email</th><th>Password</th></tr>
    </thead>
    <tbody>
      <tr><td>Admin</td><td>admin@example.com</td><td>1234</td></tr>
      <tr><td>Restaurant</td><td>chef@example.com</td><td>1234</td></tr>
      <tr><td>Delivery</td><td>driver@example.com</td><td>1234</td></tr>
      <tr><td>Customer</td><td>user@example.com</td><td>1234</td></tr>
    </tbody>
  </table>

  <h2 style="color:#33AEFF;">🖼️ Application Preview</h2>
  <pre><code>🍴 Restaurant Management System
├── 🔐 Login / Register
├── 👨‍🍳 Customer Dashboard
│   ├── View Menu
│   ├── Place Orders
│   ├── Track Status
├── 🏪 Restaurant Dashboard
│   ├── Add / Update Menu
│   ├── View Orders
├── 🚚 Delivery Dashboard
│   ├── Update Order Status
├── 🧑‍💼 Admin Dashboard
│   ├── View Users, Orders, Menu
│   ├── Delete Accounts
  </code></pre>

  <h2 style="color:#33AEFF;">🔒 Security & Validation</h2>
  <ul>
    <li>Unique email-based login credentials.</li>
    <li>Role-based access ensures user isolation.</li>
    <li>Input validations for registration and orders.</li>
    <li>Restricted database modification privileges per user role.</li>
  </ul>

  <h2 style="color:#33AEFF;">📈 Future Enhancements</h2>
  <ul>
    <li>🚀 Online Payment Integration (UPI / Credit Card)</li>
    <li>📱 Android App version using JavaFX Mobile or Flutter frontend</li>
    <li>📦 Smart Delivery Scheduling (AI-based route optimization)</li>
    <li>🌐 Cloud-based deployment with RESTful API</li>
    <li>📊 Advanced analytics dashboard for restaurant owners</li>
  </ul>

  <h2 style="color:#33AEFF;">👨‍💻 Developers</h2>
  <ul>
    <li><a href="https://www.linkedin.com/in/sarvesh-sivasankaran" target="_blank" style="color:#33AEFF;">Sarvesh S</a></li>
    <li><a href="https://www.linkedin.com/in/sherin-katherina-d/" target="_blank" style="color:#33AEFF;">Sherin Katherina D</a></li>
  </ul>

  <h2 style="color:#33AEFF;">🏁 Conclusion</h2>
  <p>This project provides a complete digital ecosystem for local restaurants to compete with large-scale food delivery systems. It brings <strong>efficiency</strong>, <strong>transparency</strong>, and <strong>automation</strong> — transforming traditional restaurant operations into a connected, modern workflow.</p>

  <h2 style="color:#33AEFF;">🪪 License</h2>
  <p>This project is licensed under the <strong>MIT License</strong> – you’re free to use, modify, and distribute it with attribution.</p>
</body>
