

//在vue里，所有的vue实例都是组件
//子局部组件，子局部组件需要在vue实例中定义后才可以使用

//组件内的template只能有一个根标签，可以用一个div套多个标签，但是不能有2个同等级的标签

const loginForm = {
    template: `
        <div>
            <h1>登录页</h1>
            用户名：<input type="text"><br>
            密&emsp;码：<input type="password"><br>
            <input type="button" value="登录">
        </div>
    `
}