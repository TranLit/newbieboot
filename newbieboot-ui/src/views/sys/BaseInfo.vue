<template>
  <div>
    <el-form>
      <el-form-item label="用户名称" :label-width="formLabelWidth">
        <el-input v-model="user.username" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="用户昵称" :label-width="formLabelWidth">
        <el-input v-model="user.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话号码" :label-width="formLabelWidth">
        <el-input v-model="user.phone" autocomplete="off"></el-input>
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
  name: "BaseInfo",
  data() {
    return {
      formLabelWidth: '80px'
    }
  },

  props: {
    user: Object
  },

  methods: {
    handleSave() {
      this.request.post('/user', this.user).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
        } else {
          this.$message.error(res.msg)
        }
      })
    },

    close() {
      router.push('/')
    }
  }
}
</script>

<style scoped>

</style>
