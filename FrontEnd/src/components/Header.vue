
<template>
    <div class="container">
        <header>
            <div class="menu-icon">
                <div class="bar"></div>
                <div class="bar"></div>
                <div class="bar"></div>
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
    }

    .menu-icon {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        width: 40px;
        height: 25px;
        cursor: pointer;
        align-items: center;
    }

    .menu-icon .bar {
        width: 25px;
        height: 3px;
        background-color: black;
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

    /*
    input[type="text"] {
        width: 80%;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }  
    */
</style>
