# spring-boot-apidoc

> 使用 apidoc 自动生成接口文档

## 环境搭建

1. 安装 node.js
2. 使用 npm 安装 apidoc `npm install apidoc –g`

## 修改apidoc

> apidoc 不支持 json 格式参数，可以通过修改 apidoc 工具代码实现

1. 修改 apidoc 工具，路径：apidoc\template\utils\send_sample_request.js

```javascript

// send AJAX request, catch success or error callback
if (param.json) {
	var ajaxRequest = {
		url: url,
        headers: header,
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(JSON.parse(param.json)),
        type: type.toUpperCase(),
        success: displaySuccess,
        error: displayError
    };
    $.ajax(ajaxRequest);
} else {
	var ajaxRequest = {
        url: url,
        headers: header,
        data: param,
        type: type.toUpperCase(),
        success: displaySuccess,
        error: displayError
    };
    $.ajax(ajaxRequest);
}

```

2. 使用 npm 安装 apidoc `npm install apidoc –g`

