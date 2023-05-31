<template>
  <div>

    <!-- 搜索区 -->
    <div class="m-10">
      <el-input class="mr-7" placeholder="请输入用户名称" style="width: 200px" suffix-icon="el-icon-search"
                v-model="username"></el-input>
      <el-input class="mr-7 ml-7" placeholder="请输入用户昵称" style="width: 200px" suffix-icon="el-icon-search"
                v-model="nickname"></el-input>
      <el-input class="mr-7 ml-7" placeholder="请输入电话号码" style="width: 200px" suffix-icon="el-icon-search"
                v-model="phone"></el-input>
      <el-button class="ml-7 mr-7" type="primary" @click="load">搜索</el-button>
      <el-button class="ml-7" type="warning" @click="reset">重置</el-button>
    </div>

    <!-- 操作按钮 -->
    <div class="m-10">
      <el-button type="primary" @click="handleAdd">新增&nbsp;<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-7 mr-7"
          confirm-button-text='确定'
          cancel-button-text='取消'
          icon="el-icon-info"
          icon-color="red"
          title="确定删除吗？"
          @confirm="handleDelBatch"
      >
        <el-button type="danger" slot="reference">批量删除&nbsp;<i class="el-icon-circle-close"></i></el-button>
      </el-popconfirm>
<!--      <el-upload
          name="file"
          action="http://localhost:8081/user/import"
          :headers="headers"
          accept=".xlsx"
          :show-file-list="false"
          :on-success="handleImportSuccess"
          :on-error="handleImportError"
          style="display: inline-block"
          class="mr-7"
      >
        <el-button type="primary">导入&nbsp;<i class="el-icon-bottom"></i></el-button>
      </el-upload>-->

<!--      <el-button type="primary" @click="window.open('http://www.baidu.com')">导出&nbsp;<i class="el-icon-top"></i></el-button>-->
    </div>

    <!-- 表格展示区 -->
    <el-table :data="tableData" border stripe header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <!-- 复选框 -->
      <el-table-column type="selection" width="50">
      </el-table-column>
      <el-table-column prop="id" label="用户编号" width="120">
      </el-table-column>
      <el-table-column prop="username" label="用户名称">
      </el-table-column>
      <el-table-column prop="nickname" label="用户昵称">
      </el-table-column>
      <el-table-column prop="phone" label="电话号码">
      </el-table-column>
      <el-table-column prop="status" label="用户状态" width="100">
        <template v-slot="scope">
          <el-switch
              v-model="scope.row.status"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0"
              @change="changeStatus($event, scope.row)"
          >
          </el-switch>
        </template>
      </el-table-column>

      <el-table-column prop="role.name" label="用户角色">
      </el-table-column>

      <el-table-column prop="createTime" label="创建时间" width="200">
      </el-table-column>

      <el-table-column label="操作" width="210">
        <template v-slot="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑&nbsp;<i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-7"
              confirm-button-text='确定'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              title="确定删除吗？"
              @confirm="handleDelete(scope.row.id)"
          >
            <el-button slot="reference" type="danger">删除&nbsp;<i class="el-icon-circle-close"></i></el-button>
          </el-popconfirm>

        </template>
      </el-table-column>
    </el-table>


    <!-- 分页、页码 -->
    <div class="pd-10">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <!-- 嵌套表单的dialog -->
    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
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
        <el-form-item label="用户角色" :label-width="formLabelWidth">
          <el-select
              clearable
              :value="user.role.name"
              placeholder="请选择"
              style="width: 100%"
              @change="handleRoleChange"
          >
            <el-option v-for="item in roles" :key="item.id" :label="item.name" :value="item">

            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: "User",
  data() {
    return {
      headers: {
        token: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
      },
      multipleSelection: [],
      total: 0,
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      username: '',
      nickname: '',
      phone: '',
      user: {
        role: {}
      },
      dialogFormVisible: false,
      formLabelWidth: '80px',
      roles: {}
    }
  },

  created() {
    this.load();
    this.request.get('/role').then(res => {
      if (res.code === '200') {
        this.roles = res.data;
      } else {
        this.$message.error(res.msg);
      }
    });
  },

  methods: {
    handleRoleChange(item) {
      this.user.role = item;
    },

    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },

    handleAdd() {
      this.dialogFormVisible = true;
      this.user = {
        role: {}
      };
    },

    handleEdit(row) {
      // this.user = row;
      // 防止表格里的数据随着表单数据的改变实时改变
      this.user = Object.assign({}, row);
      this.dialogFormVisible = true;
    },

    handleDelete(id) {
      this.request.delete('user/' + id).then(res => {
        if (res.code === '200') {
          this.$message.success('删除成功');
          this.load();
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    handleDelBatch() {
      // 将一个对象的数组转换成对象中某个属性的数组
      let ids = this.multipleSelection.map(v => v.id);
      this.request.delete('/user/delete/batch', {data: ids}).then(res => {
        if(res.code === '200') {
          this.$message.success('批量删除成功');
          this.load();
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    /*
    模糊分页查询用户信息
     */
    load() {
      this.request.get('/user/page', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          nickname: this.nickname,
          phone: this.phone
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.records
          this.total = res.data.total
        } else {
          this.$message.error('用户信息加载失败')
        }
      })
    },

    reset() {
      this.username = '';
      this.nickname = '';
      this.phone = '';
      this.load();
    },

    cancel() {
      this.dialogFormVisible = false;
      this.load();
    },

    save() {
      this.request.post('/user', this.user).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功');
          this.dialogFormVisible = false;
          this.load();
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    /*exp() {
      this.request.post('/user/export',
          {
            responseType: 'blob',
            headers: {
              'Content-Disposition': "attachment; filename=template.xlsx",
              'Content-Type': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8',
            }
          })
          .then((res) => {
            const url = window.URL.createObjectURL(new Blob([res.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'template.xlsx');
            document.body.appendChild(link);
            link.click();
          })
          .catch((error) => console.log(error));
    },*/

    handleImportSuccess() {
      this.$message.success('导入成功');
      this.load();
    },

    handleImportError() {
      this.$message.error('导入失败');
    },

    changeStatus(e, row) {
      row.status = e;
      this.request.post('/user', row).then(res => {
        if (res.code === '200') {
          this.load();
        } else {
          this.$message.error('状态修改失败')
        }
      })
    }
  }
}
</script>

<style>
.headerBg {
  background: #eee !important;
}
</style>
