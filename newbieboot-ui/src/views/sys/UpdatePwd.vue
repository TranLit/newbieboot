<template>
  <div>
    <el-form :rules="rules" :model="pwd" ref="userForm">
      <el-form-item label="旧密码" prop="oldPwd" :label-width="formLabelWidth">
        <el-input show-password placeholder="请输入旧密码" v-model="pwd.oldPwd" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPwd" :label-width="formLabelWidth">
        <el-input show-password placeholder="请输入新密码" v-model="pwd.newPwd" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPwd" :label-width="formLabelWidth">
        <el-input show-password placeholder="请输入新密码" v-model="pwd.confirmPwd" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>

    <div style="margin-left: 80px">
      <el-button type="primary" @click="handleSave">保 存</el-button>
      <el-button type="danger" @click="close">关 闭</el-button>
    </div>
  </div>
</template>

<script>
import router from "../../router";

export default {
  name: "UpdatePwd",
  props: {
    user: Object
  },
  data() {
    // 确认密码校验规则
    const equalToPassword = (rule, value, callback) => {
      if (this.pwd.newPwd !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    // 旧密码校验规则
    const equalOldPassword = (rule, value, callback) => {
      if (this.user.password !== value) {
        callback(new Error("旧密码不正确"));
      } else {
        callback();
      }
    };
    return {
      formLabelWidth: '80px',
      pwd: {
        oldPwd: undefined,
        newPwd: undefined,
        confirmPwd: undefined
      },
      rules: {
        oldPwd: [
          {required: true, message: '旧密码不能为空', trigger: 'blur'},
          {required: true, validator: equalOldPassword, trigger: 'blur'}
        ],
        newPwd: [
          {required: true, message: '新密码不能为空', trigger: 'blur'},
          {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        confirmPwd: [
          {required: true, message: '确认密码不能为空', trigger: 'blur'},
          {required:true, validator: equalToPassword, trigger: 'blur'}
        ]
      }
    }
  },

  methods: {
    handleSave() {
      this.$refs['userForm'].validate(valid => {
        if (valid) {
          // 发送请求修改密码
          this.user.password = this.pwd.newPwd;
          this.request.post('/user', this.user).then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功')
            } else {
              this.$message.error(res.msg)
            }
          });
        }
      });
    },

    close() {
      router.push('/')
    }

  }
}
</script>

<style scoped>

</style>
