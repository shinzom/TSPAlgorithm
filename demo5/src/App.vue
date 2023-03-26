<script setup>
</script>

<template>
    <div>
    <p>
        <router-link to="/">Go To Home</router-link>
        <router-link to="/Show">Go To Show</router-link>  
    </p>
    <router-view></router-view>
    </div>
</template>

<style>
    @import "./style/common.scss";
    #app {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
    }
</style>