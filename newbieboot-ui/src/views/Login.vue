<template>
  <div class="wrapper">
    <div class="loginForm" style="margin: 260px auto; background-color: #fff; width: 400px; height: 390px; padding: 20px; border-radius: 10px">
      <div style="padding-bottom: 20px; margin: 10px 0; text-align: center; font-size: 24px; font-family: '微软雅黑'; color: #707087">电商管理系统</div>
      <el-form :rules="rules" :model="user" ref="userForm">
        <el-form-item prop="username">
          <el-input placeholder="请输入账号" size="medium" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input placeholder="请输入密码" size="medium" prefix-icon="el-icon-lock" v-model="user.password" show-password></el-input>
        </el-form-item>

        <el-form-item prop="captcha">
          <el-input placeholder="请输入验证码" size="medium" prefix-icon="el-icon-picture-outline" v-model="user.captcha" style="width: 180px"></el-input>
        </el-form-item>
        <img
            style="cursor: pointer; height:36px; display: inline-block; position:relative; left: 200px; top: -60px"
            :src="'data:image/*;base64,' + captchaImg"
            alt="验证码"
            @click="replaceCaptcha"
        >
        <el-form-item style="margin-top: -50px">
          <el-checkbox v-model="rememberMe">记住密码</el-checkbox>
        </el-form-item>
        <el-form-item style="text-align: center">
          <el-button type="primary" autocomplete="off" @click="login" style="width: 100%">登录</el-button>
        </el-form-item>
      </el-form>

    </div>
  </div>
</template>

<script>

import Cookies from "js-cookie"

export default {
  name: "Login",
  data() {
    return {
      user: {},
      captchaImg: '',
      rememberMe: false,
      rules: {
        username: [
          {required: true, message: '用户名不能为空', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '密码不能为空', trigger: 'blur'},
          {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ],
        captcha: [
          {required: true, message: '验证码不能为空', trigger: 'blur'}
        ]
      }
    }
  },

  created() {
    this.getCaptcha();
    this.getCookie();
  },

  methods: {
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      this.user = {
        username: username === undefined ? this.user.username : username,
        password: password === undefined ? this.user.password : password
      };
      this.rememberMe = rememberMe === undefined ? false : Boolean(rememberMe)
    },

    replaceCaptcha() {
      this.getCaptcha();
    },

    getCaptcha() {
      this.request.get('/common/captchaImage').then(res => {
        if (res.code === '200') {
          this.user.uuid = res.data.uuid
          this.captchaImg = res.data.img
        } else {
          this.$message.error('服务器异常')
        }
      })
    },

    login() {
      this.$refs['userForm'].validate(valid => {
        if (valid) {
          /* 表单校验合法 */
          this.request.post('/user/login', this.user).then(res => {
            if (res.code === '200') {
              /* 将用户信息存储到浏览器当中 */
              localStorage.setItem('user', JSON.stringify(res.data));
              this.$router.push('/');
              this.$message.success('登录成功');
            } else {
              this.$message.error(res.msg);
            }
          })

          /* 记住密码 */
          if (this.rememberMe) {
            Cookies.set("username", this.user.username, { expires: 30 });
            Cookies.set("password", this.user.password, { expires: 30 });
            Cookies.set('rememberMe', this.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
        }
      })
    }
  }
}
</script>

<style scoped>
  .wrapper {
    height: 100vh;
    /*渐变背景色*/
    /*background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);*/
    /*background-image: linear-gradient(to bottom right, #2980B9, #C6FFDD, #6DD5FA, #f7797d, #FBD786);*/


    /*固定背景图*/
    /*background: url("../assets/bg.jpg");*/
    background: url("../assets/login-background-1.jpg");
    background-size: cover;
    overflow: hidden;
  }
  .loginForm {
    background-color: rgba(238, 241, 246);
    box-shadow: 2px 0 6px rgb(0 21 41 / 60%);
    opacity: .97;
  }
</style>
