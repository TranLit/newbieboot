<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="24">
        <blockquote class="text-warning" style="font-size: 14px">
          <h2>NewbieBoot后台管理框架</h2>
          <br>
          <p>大三下半年想提前将毕业设计这个麻烦事给落实了。通过网上查阅资料发现大多数同学的毕业设计都是基于SpringBoot + Vue的前后端分离模式开发的后台管理系统，比如：图书管理系统、宿舍管理系统、学生管理系统、电商后台管理系统等等。网上也有很多非常优秀的开源管理系统框架可供选择，但是经过我的不断尝试发现了一个很现实的问题，那就是这些优秀开源框架过于优秀，代码封装的非常好，且大部分后台管理系统该有的它们都有，这就导致了对于学生群体来说上手有点困难，学习成本较高。所以我就想根据它们这些优秀的框架自己写一个适合学生群体拿来做毕设的框架，于是利用空闲休息时间开始自己写一套后台系统。如此有了NewbieBoot后台管理系统。newbie 中文意思为 "菜鸟"，而 "boot" 在英语中是指引导、启动的意思。NewbieBoot的含义就是让每一个小白都可以直接上手做毕设。</p>
          <br>
          <br>
          <p>
            <el-tag type="danger" style="margin-right: 10px">&yen;免费开源</el-tag>
            <el-button
                type="primary"
                size="mini"
                icon="el-icon-cloudy"
                plain
                @click="goTarget('')"
            >
              访问码云
            </el-button>
            <el-button
                size="mini"
                icon="el-icon-cloudy"
                plain
                @click="goTarget('')"
            >
              访问GitHub
            </el-button>
          </p>
        </blockquote>

        <hr />
      </el-col>
    </el-row>
    <el-row :gutter="20">


      <el-col :sm="24" :lg="12" style="padding-left: 30px">
        <el-row>
          <el-col :span="12">
            <h2>技术选型</h2>
          </el-col>
        </el-row>
        <el-row style="margin-left: 80px">
          <el-col :span="13">

            <h4>后端技术</h4>
            <ul>
              <li>SpringBoot</li>
              <li>JWT</li>
              <li>Knife4j</li>
              <li>MySQL</li>
              <li>Redis</li>
              <li>Mybatis</li>
              <li>Mybatis-plus</li>
              <li>Hutool</li>
              <li>EasyExcel</li>
              <li>Druid</li>
              <li>aliyun-sdk-oss</li>
              <li>kaptcha</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>前端技术</h4>
            <ul>
              <li>Vue</li>
              <li>Element-ui</li>
              <li>Axios</li>
              <li>Js-cookie</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>联系信息</span>
          </div>
          <div class="body">
            <p>
              <i class="el-icon-chat-dot-round"></i> 微信：moumoumomo_
            </p>
            <p>
              <i class="el-icon-chat-dot-square"> QQ：3500373755</i>
            </p>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log" style="height: 400px; width: 700px">
          <div slot="header" class="clearfix">
            <span>捐赠支持</span>
          </div>
          <div class="body" style="margin-top: 20px; display: inline-block">
            <img
                src="https://ianlit-picture-beijing.oss-cn-beijing.aliyuncs.com/img/20230531005250.png"
                alt="donate"
                width="300px"
                style="margin-top: -30px"
            />
          </div>
          <div class="body" style="margin-top: 20px; margin-left: 30px; display: inline-block">
            <img
                src="https://ianlit-picture-beijing.oss-cn-beijing.aliyuncs.com/img/20230531005629.png"
                alt="donate"
                width="300px"
                height="307px"
                style="margin-top: -30px"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      // 版本号
      version: "3.8.5"
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    }
  }
};
</script>

<style scoped>
blockquote {
  padding: 10px 20px;
  margin: 0 0 20px;
  font-size: 17.5px;
  border-left: 5px solid #eee;
}
hr {
  margin-top: 20px;
  margin-bottom: 20px;
  border: 0;
  border-top: 1px solid #eee;
}
.col-item {
  margin-bottom: 20px;
}

ul {
  padding: 0;
  margin: 0;
}

.home {
  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;
}

ul {
  list-style-type: none;
}

h4 {
  margin-top: 10px;
  font-size: 17px;
}

h2 {
  margin-top: 10px;
  font-size: 30px;
  font-weight: 100;
}

p {
  margin-top: 10px;
}

b {
  font-weight: 700;
}

ol {
  display: block;
  list-style-type: decimal;
  margin-block-start: 1em;
  margin-block-end: 1em;
  margin-inline-start: 0;
  margin-inline-end: 0;
  padding-inline-start: 40px;
}
</style>

