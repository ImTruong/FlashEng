<script setup>
    import { ref } from 'vue';

    const menuOpen = ref(false);

    const toggleMenu = () => {
    menuOpen.value = !menuOpen.value;
    };
</script>

<template>
    <div class="container">
        <header>
            <div class="nav-header">
                <button class="nav-toggle" @click="toggleMenu">☰</button>
            </div>
            <div v-if="menuOpen" class="nav-menu">
                <ul>
                    <li>
                        <img src="@/assets/icons/home-icon.png" alt="Home"  class="menu-icon"/>
                        <a href="#">Home</a>
                    </li>
                    <li>
                        <img src="@/assets/icons/folder.png" alt="folder"  class="menu-icon"/>
                        <a href="#">Your library</a>
                    </li>
                    <li>
                        <img src="@/assets/icons/statistics.png" alt="Home"  class="menu-icon"/>
                        <a href="#">Statistics</a>
                    </li>
                    <li>
                        <img src="@/assets/icons/notification.png" alt="Home"  class="menu-icon"/>
                        <a href="#">Notifications</a>
                    </li>
                    <li>
                        <img src="@/assets/icons/user.png" alt="Home"  class="menu-icon"/>
                        <a href="#">Your profile</a>
                    </li>
                    <li>
                        <img src="@/assets/icons/logout.png" alt="Home"  class="menu-icon"/>
                        <a href="#">Log out</a>
                    </li>
                </ul>
            </div>
            <input type="text" placeholder="Search for flashcards" class="search-bar"/>
            <div class="button-container">
                <button class="add-button" @click="">+</button>
                <div class="user-icon">
                    U
                </div>
            </div>
        </header>
    </div>
</template>

<style scoped>
    header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 5px;
        background-color: #DBF3F7;
        border-bottom: 1px solid #ccc;
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        z-index: 9999; /* Đảm bảo header luôn nằm trên các phần tử khác */
    }
        
    .nav-header {
        display: flex;
        align-items: center;
    }

    .nav-toggle {
        background: none;
        border: none;
        font-size: 24px;
        cursor: pointer;
        color: #333;
    }

    .nav-menu {
        position: absolute;
        top: 50px;
        left: 0;
        width: 250px;
        height: 100vh;
        background-color: #fff;
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
        display: flex;
        justify-content: center;
        flex-direction: column;
        justify-content: flex-start;
        padding: 20px;
        z-index: 9999;
    }

    .menu-icon {
        width: 24px;  /* Chỉnh kích thước rộng */
        height: 24px; /* Chỉnh kích thước cao */
        margin-right: 10px;
    }  

    .nav-menu ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .nav-menu ul li {
        display: flex;
        margin: 10px 0;
        align-items: center;
    }

    .nav-menu ul li a {
        text-decoration: none;
        color: #333;
        font-size: 14px;
        padding: 10px;
        display: block;
        font-weight: 500;
    }

    .nav-menu ul li a:hover {
        background-color: rgba(128, 128, 128, 0.263);
        color: white;
        width: 100%;
    }
    .button-container {
        display: flex; /* Sử dụng flexbox cho container */
        align-items: center; /* Căn giữa dọc */
        margin-left: 10px; /* Khoảng cách bên trái */
    }

    .user-icon {
        font-size: 16px;
        background-color: #D9D9D9; /* Màu nền cho icon user */
        color: white; /* Màu chữ */
        width: 40px; /* Kích thước rộng của icon */
        height: 40px; /* Kích thước cao của icon */
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 50%; /* Tạo hình tròn */
        margin-left: 10px; /* Tạo khoảng cách giữa add-button và user icon */
        cursor: pointer;
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
    }

    .add-button {
        font-size: 20px;
        background-color: #BDEDF5;
        color: Black;
        border: none;
        padding: 0; /* Đặt padding thành 0 để giảm kích thước */
        border-radius: 4px;
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
        transition: box-shadow 0.3s ease;
    }

    .search-bar {
        width: 60%;
        height: 40px;
        padding: 0 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
    }
</style>
