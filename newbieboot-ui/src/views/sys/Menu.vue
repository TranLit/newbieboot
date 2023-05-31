<template>
  <div>

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
    </div>

    <!-- 表格展示区 -->
    <el-table :data="tableData"
              border
              stripe
              header-cell-class-name="headerBg"
              @selection-change="handleSelectionChange"
              row-key="id"
              default-expand-all
    >
      <!-- 复选框 -->
      <el-table-column type="selection" width="50">
      </el-table-column>
      <el-table-column prop="id" label="菜单编号">
      </el-table-column>
      <el-table-column prop="name" label="菜单名称">
      </el-table-column>
      <el-table-column prop="path" label="菜单路径">
      </el-table-column>
      <el-table-column label="菜单图标" align="center" width="200">
        <template v-slot="scope">
          <i :class="scope.row.icon" style="font-size: 28px"></i>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="菜单描述">
      </el-table-column>
      <el-table-column prop="pid" label="父级菜单编号">
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

    <!-- 嵌套表单的dialog -->
    <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form>
        <el-form-item label="菜单名称" :label-width="formLabelWidth">
          <el-input v-model="menu.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="菜单路径" :label-width="formLabelWidth">
          <el-input v-model="menu.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="菜单图标" :label-width="formLabelWidth">
          <el-select clearable v-model="menu.icon" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in icons" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value"></i>&nbsp;{{item.name}}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="菜单描述" :label-width="formLabelWidth">
          <el-input v-model="menu.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="父级菜单编号" :label-width="formLabelWidth">
          <el-input v-model="menu.pid" autocomplete="off"></el-input>
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
  name: "Role",
  data() {
    return {
      headers: {
        token: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
      },
      multipleSelection: [],
      tableData: [],
      menu: {},
      icons: {},
      dialogFormVisible: false,
      formLabelWidth: '100px'
    }
  },

  created() {
    this.load();
  },

  methods: {

    handleAdd() {
      this.dialogFormVisible = true;
      this.menu = {};
    },

    handleEdit(row) {
      // 防止表格里的数据随着表单数据的改变实时改变
      this.menu = Object.assign({}, row);
      this.dialogFormVisible = true;

      this.request.get('/dict', {
        params: {
          type: 'icon'
        }
      }).then(res => {
        this.icons = res.data;
      })

    },

    handleDelete(id) {
      this.request.delete('/menu/' + id).then(res => {
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
      this.request.delete('/menu/delete/batch', {data: ids}).then(res => {
        if(res.code === '200') {
          this.$message.success('批量删除成功');
          this.load();
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    /*
    查询所有菜单信息
     */
    load() {
      this.request.get('/menu').then(res => {
        if (res.code === '200') {
          this.tableData = res.data
        } else {
          this.$message.error('菜单信息加载失败')
        }
      })
    },

    cancel() {
      this.dialogFormVisible = false;
      this.load();
    },

    save() {
      this.request.post('/menu', this.menu).then(res => {
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
  }
}
</script>

<style>
.headerBg {
  background: #eee !important;
}
</style>
