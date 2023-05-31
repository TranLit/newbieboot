<template>
  <div>
    <el-menu :default-openeds="['1', '3']" style="min-height: 110vh; overflow-x: hidden"
             background-color="rgb(48, 65, 86)"
             text-color="#ffffff"
             active-text-color="#409EFF"
             :collapse-transition="false"
             :collapse="isCollapse"
             router
    >
      <div style="height: 60px; line-height: 60px; text-align: center">
        <img class="mr-7" src="../../assets/logo.png"
             style="width: 20px; position: relative; top: 4px; margin-left: 6px">
        <b style="color: white; font-size: 18px" v-show="logoTextShow">NewbieBoot</b>
      </div>

      <div v-for="item in menus" :key="item.id + ''">
        <!-- 若无子菜单，icon图标应放在template外部 -->
        <el-menu-item v-if="item.path" :index="item.path" class="el-menu-item">
          <i :class="item.icon"></i>
          <template slot="title">
            <span v-show="!isCollapse" slot="title">{{ item.name }}</span>
          </template>
        </el-menu-item>
        <!-- 注意，这里的index不可以是数字，必须是字符串 -->
        <el-submenu v-else :index="item.id + ''">
          <template slot="title">
            <i :class="item.icon"></i>
            <span v-show="!isCollapse" slot="title">{{ item.name }}</span>
          </template>
          <div v-for="child in item.children" :key="child.id">
            <el-menu-item :index="child.path" class="el-menu-item">
              <template>
                <i :class="child.icon"></i>
                <span v-show="!isCollapse" slot="title">{{ child.name }}</span>
              </template>
            </el-menu-item>
          </div>
        </el-submenu>
      </div>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "Aside",

  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  },

  data() {
    return {
      menus: []
    }
  },

  created() {
    this.menus = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).menus : []
  }
}
</script>

<style scoped>
  .el-menu-item:hover {
    background-color: #05192B !important;
  }

  /deep/ .el-submenu__title:hover {
    background-color: #05192B !important;
  }
</style>
