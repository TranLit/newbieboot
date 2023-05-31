<template>
  <div>
    <el-card style="position: relative; width: 500px; height: 600px; display: inline-block; ">
      <div slot="header">
        <span>个人信息</span>
      </div>

      <el-upload
          action="http://localhost:8081/user/uploadAvatar"
          :headers="headers"
          :data="{id: user.id}"
          name="avatar"
          :on-success="uploadSuccess"
          :on-error="uploadError"
      >
        <el-image
            style="width: 150px; height: 150px; border-radius: 50%; margin-left: 165px; margin-bottom: 20px; cursor: pointer; border: 1px solid #05192B"
            :src="user.avatarUrl"
            fit="fit"
        ></el-image>
      </el-upload>



      <hr color="#E7EAEC" size="1">
      <div class="item" style="margin: 5px 0; font-size: 15px;">
        <div class="left"><i class="el-icon-user-solid"></i>用户名称</div>
        <div class="right">{{ user.username }}</div>
      </div>

      <hr color="#E7EAEC" size="1">
      <div class="item" style="margin: 5px 0; font-size: 15px;">
        <div class="left"><i class="el-icon-edit"></i>用户昵称</div>
        <div class="right">{{ user.nickname }}</div>
      </div>
      <hr color="#E7EAEC" size="1">
      <div class="item" style="margin: 5px 0; font-size: 15px;">
        <div class="left"><i class="el-icon-phone"></i>电话号码</div>
        <div class="right">{{ user.phone }}</div>
      </div>
      <hr color="#E7EAEC" size="1">
      <div class="item" style="margin: 5px 0; font-size: 15px;">
        <div class="left"><i :class="statusIcon"></i>用户状态</div>
        <div class="right">{{ user.status ? '启用' : '禁用' }}</div>
      </div>
      <hr color="#E7EAEC" size="1">
      <div class="item" style="margin: 5px 0; font-size: 15px;">
        <div class="left"><i class="el-icon-date"></i>创建时间</div>
        <div class="right">{{ user.createTime }}</div>
      </div>
      <hr color="#E7EAEC" size="1">
      <div class="item" style="margin: 5px 0; font-size: 15px;">
        <div class="left"><i class="el-icon-s-custom"></i>所属角色</div>
        <div class="right">{{user.role.name}}</div>
      </div>
      <hr color="#E7EAEC" size="1">




    </el-card>

    <el-card style="position: absolute; top: 80px; margin-left: 20px; width: 500px; height: 420px; display: inline-block">
      <div slot="header" class="clearfix">
        <span>修改资料</span>
      </div>
      <el-menu default-active="/personInfo/baseInfo" class="el-menu-demo" mode="horizontal"
            style="margin-top: -20px; margin-bottom: 30px; margin-left: -10px"
            router
      >
        <el-menu-item index="/personInfo/baseInfo">基本信息</el-menu-item>
        <el-menu-item index="/personInfo/updatePwd">修改密码</el-menu-item>
      </el-menu>
      <div class="line"></div>
      <router-view :user="user"/>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "PersonInfo",
  data() {
    return {
      statusIcon: '',
      user: {},
      headers: {
        token: undefined
      }
    }
  },
  created() {
    this.user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null
    if (this.user !== null) {
      this.headers.token = this.user.token
      // 根据用户状态设置icon图标
      if (this.user.status === 1) {
        this.statusIcon = 'el-icon-unlock'
      } else {
        this.statusIcon = 'el-icon-lock'
      }
    } else {
      this.$message.error('个人信息加载失败');
    }
  },

  methods: {
    uploadSuccess(res) {
      console.log(this.user);
      if (res.code === '200') {
        this.user.avatarUrl = res.data;
        // 注意localStorage里只可以放字符串，所以需要将对象转化为JSON字符串
        localStorage.setItem('user', JSON.stringify(this.user))
        this.$alert('头像更换成功，点击确定刷新当前页面即可生效', '提示', {
          confirmButtonText: '确定',
          callback: action => {
            location.reload()
          }
        });
      } else {
        this.$message.error(res.msg)
      }
    },
    uploadError(res) {
      this.$message.error(res.msg)
    }
  }

}
</script>

<style scoped>
.item {
  display: flex;
  justify-content: space-between;
}

.item .left {
  flex: 1;
}

</style>
