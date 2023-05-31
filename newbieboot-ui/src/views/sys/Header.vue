<template>
  <div style="line-height: 60px; display: flex">

    <div style="flex: 1">
      <!-- collapse按钮 -->
      <span :class="collapseBtnClass" style="font-size: 25px; cursor: pointer" @click="collapse"></span>

    </div>

    <Breadcrumb style="flex: 30; line-height: 60px"/>

    <el-dropdown style="width: 200px; cursor: pointer">
      <div>
        <img :src="user.avatarUrl" alt="" style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
        <span>{{user.nickname}}</span>
        <i class="el-icon-arrow-down ml-7"></i>
      </div>

      <el-dropdown-menu slot="dropdown" style="width: 150px">
        <el-dropdown-item>
          <div @click="$router.push('/personInfo')">个人信息</div>
        </el-dropdown-item>
        <el-dropdown-item>
          <div @click="logout">退出</div>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
import Breadcrumb from "./Breadcrumb";
export default {
  name: "Header",
  components: {Breadcrumb},
  props: {
    collapseBtnClass: String,
    collapse: Function
  },
  data() {
    return {
      user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {}
    }
  },
  methods: {
    logout() {
      this.$confirm('此操作会退出当前用户，是否继续?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$router.push('/login');
        localStorage.removeItem('user');
        this.$message.success('退出成功');
      }).catch(() => {
        this.$message.info('已取消退出');
      });
    }
  }
}
</script>

<style scoped>

</style>

