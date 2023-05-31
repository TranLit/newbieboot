<template>
  <div>
    <el-container style="overflow: hidden; height: 110vh">
      <!--侧边栏-->
      <el-aside :width="sideWidth + 'px'" style="background-color: rgba(238, 241, 246); box-shadow: 2px 0 6px rgb(0 21 41 / 35%)">
        <Aside :is-collapse="isCollapse" :logo-text-show="logoTextShow"/>
      </el-aside>

      <!--主体-->
      <el-container>

        <!--Header-->
        <el-header style="border-bottom: 1px solid #ccc">
          <Header :collapse-btn-class="collapseBtnClass" :collapse="collapse"/>
        </el-header>
        <!-- 主内容区 -->
        <el-main>
          <!-- 表示当前页面的子路由会在router-view里面展示 -->
          <router-view/>
        </el-main>

      </el-container>
    </el-container>
  </div>
</template>

<script>

/* 如果没有引入的话，就需要使用this.request的方式(已经在main.js里配置好了) */
// import request from "@/utils/request";
import Aside from "@/views/sys/Aside";
import Header from "@/views/sys/Header";

export default {
  name: 'Manage',
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 240,
      logoTextShow: true,
    }
  },

  components: {
    Aside,
    Header
  },

  methods: {
    collapse() {
      this.isCollapse = !this.isCollapse;
      if (this.isCollapse) {
        // 收缩
        this.collapseBtnClass = 'el-icon-s-unfold';
        this.sideWidth = 64;
        this.logoTextShow = false;
      } else {
        // 展开
        this.collapseBtnClass = 'el-icon-s-fold';
        this.sideWidth = 200;
        this.logoTextShow = true;
      }
    },
  }
}
</script>
