<template>
  <div>

    <!-- 搜索区 -->
    <div class="m-10">
      <el-input class="mr-7" placeholder="请输入角色名称" style="width: 200px" suffix-icon="el-icon-search"
                v-model="name"></el-input>
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
    </div>

    <!-- 表格展示区 -->
    <el-table :data="tableData"
              border
              stripe
              header-cell-class-name="headerBg"
              @selection-change="handleSelectionChange"
    >
      <!-- 复选框 -->
      <el-table-column type="selection" width="50">
      </el-table-column>
      <el-table-column prop="id" label="角色编号" width="120">
      </el-table-column>
      <el-table-column prop="name" label="角色名称">
      </el-table-column>
      <el-table-column prop="roleKey" label="角色标识">
      </el-table-column>

      <el-table-column label="操作" width="335">
        <template v-slot="scope">
          <el-button type="info" @click="selectMenu(scope.row.id)">菜单分配&nbsp;<i class="el-icon-menu"></i></el-button>
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

    <!-- 修改或添加角色的dialog -->
    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form>
        <el-form-item label="角色名称" :label-width="formLabelWidth">
          <el-input v-model="role.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色标识" :label-width="formLabelWidth">
          <el-input v-model="role.roleKey" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 菜单分配的dialog -->
    <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%">
      <el-tree
          :data="menuData"
          show-checkbox
          node-key="id"
          :default-checked-keys="checks"
          default-expand-all
          :props="props"
          ref="menuTree"
      >
        <span class="custom-tree-node" slot-scope="{node, data}">
          <i :class="data.icon"></i>{{data.name}}
        </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="grant">确 定</el-button>
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
      total: 0,
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      name: '',
      role: {},
      dialogFormVisible: false,
      menuDialogVis: false,
      props: {
        label: 'name',
        children: 'children'
      },
      menuData: [],
      formLabelWidth: '80px',
      roleId: 0,
      menuIds: [],
      checks: []
    }
  },

  created() {
    this.load();
  },

  methods: {
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
      this.role = {};
    },

    handleEdit(row) {
      // this.user = row;
      // 防止表格里的数据随着表单数据的改变实时改变
      this.role = Object.assign({}, row);
      this.dialogFormVisible = true;
    },

    selectMenu(roleId) {
      this.roleId = roleId;
      this.menuDialogVis = true;
      // 请求菜单信息
      this.request.get('/menu').then(res => {
        if (res.code === '200') {
          this.menuData = res.data;
        } else {
          this.$message.error('菜单加载失败');
        }
      });
      // 请求已分配菜单id
      this.request.get('/role/' + this.roleId).then(res => {
        if (res.code === '200') {
          this.checks = res.data;
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    handleDelete(id) {
      this.request.delete('/role/' + id).then(res => {
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
      this.request.delete('/role/delete/batch', {data: ids}).then(res => {
        if(res.code === '200') {
          this.$message.success('批量删除成功');
          this.load();
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    /*
    模糊分页查询角色信息
     */
    load() {
      this.request.get('/role/page', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          description: this.description
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.records
          this.total = res.data.total
        } else {
          this.$message.error('角色信息加载失败')
        }
      })
    },

    reset() {
      this.name = '';
      this.description = '';
      this.load();
    },

    cancel() {
      this.menuDialogVis = false;
      // 清空checks，防止下次打开默认显示
      this.checks = [];
      this.dialogFormVisible = false;
      this.load();
    },

    save() {
      this.request.post('/role', this.role).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功');
          this.dialogFormVisible = false;
          this.load();
        } else {
          this.$message.error(res.msg);
        }
      })
    },

    grant() {
      this.menuIds = this.$refs.menuTree.getCheckedKeys()
      this.$refs.menuTree.getHalfCheckedKeys().map(m => this.menuIds.push(m))
      this.request.post('/role/grant/' + this.roleId, this.menuIds).then(res => {
        if (res.code === '200') {
          this.$message.success('分配成功');
        } else {
          this.$message.error(res.msg);
        }
      });
      this.menuDialogVis = false;
      // 清空checks，防止下次打开默认显示
      this.checks = [];
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
