

//在vue里，所有的vue实例都是组件
//子局部组件，子局部组件需要在vue实例中定义后才可以使用

//组件内的template只能有一个根标签，可以用一个div套多个标签，但是不能有2个同等级的标签

const registForm = {
    template: `
        <div>
            <h1>注册页</h1>
            用&ensp;户&ensp;名：<input type="text"><br>
            密&emsp;&emsp;码：<input type="password"><br>
            确认密码：<input type="password"><br>
            <input type="button" value="注册">
        </div>
    `
}