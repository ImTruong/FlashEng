<template>
    <header>
        <input type="text" placeholder="Search for flashcards"  />
        <button @click="">+</button>
    </header>
</template>

<style scoped>
    header {
        display: flex;
        justify-content: space-between;
        padding: 10px;
        background-color: #DBF3F7;
        border-bottom: 1px solid #ccc;
    }

    input[type="text"] {
        width: 80%;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }
</style>