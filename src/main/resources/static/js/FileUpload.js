WebUploader.Uploader.register({
    'before-send-file': 'beforeSendFile',
    'before-send': 'beforeSend',
    'after-send-file': 'afterSendFile'
}, {
    beforeSendFile: function (file) {
        var deferred = WebUploader.Deferred();
        this.owner.md5File(file, 0, 10240)
            .then(function (val) {
                $.ajax({
                    url: '/fileMD5/' + val,
                    type: 'GET',
                    success: function () {
                        uploader.options.formData = {'md5': val};
                        file.rel = val;
                        deferred.resolve();
                    }
                });
            });
        file.startTime = new Date();
        return $.when(deferred);
    },
    beforeSend: function (block) {
        var deferred = WebUploader.Deferred();
        $.ajax({
            url: '/chunk/' + block.chunk,
            type: 'GET',
            data: {
                md5: block.file.rel
            },
            success: function (result) {
                if (result.success) {
                    deferred.resolve();
                } else {
                    if (block.chunk != block.chunks - 1) {
                        deferred.reject();
                    } else {
                        deferred.resolve();
                    }
                }
            }
        })
        return $.when(deferred);
    },
    afterSendFile: function (file) {
        var $li = $('#' + file.id);
        $.ajax({
            url: '/upload/' + file.rel,
            type: 'POST',
            success: function () {
                $li.find('p.state').text('文件上传完成');
            }
        });
    }
});
var uploader = WebUploader.create({

    // swf文件路径
    swf: 'http://localhost:8080/webuploader-0.1.5/Uploader.swf',

    // 文件接收服务端。
    server: '/upload',

    // 选择文件的按钮。可选。
    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
    pick: '#picker',
    chunked: true,
    chunkSize: 5242880,
    threads: 3,
    prepareNextFile: true,
    duplicate: true,

    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
    resize: false
});
uploader.on('fileQueued', function (file) {
    $('#thelist').append('<div id="' + file.id + '" class="item">' +
        '<h4 class="info">' + file.name + '</h4>' +
        '<p class="state">等待上传...</p>' +
        '</div>');
});
uploader.on('uploadProgress', function (file, percentage) {
    var $li = $('#' + file.id), $percent = $li.find('.progress .progress-bar');

    // 避免重复创建
    if (!$percent.length) {
        $percent = $('<div class="progress progress-striped active">' +
            '<div class="progress-bar" role="progressbar" style="width: 0%">' +
            '</div>' +
            '</div>').appendTo($li).find('.progress-bar');
    }
    var fileSize = (file.size / (1024 * 1024)).toFixed(2);
    var downloaded = (fileSize * percentage).toFixed(2);
    var speed = (downloaded / (new Date().valueOf() - file.startTime.valueOf()) * 1000 * 1024).toFixed(2);
    var state = '上传中...' + fileSize + 'MB : ' + downloaded + 'MB / ';
    if (speed / 1024 > 1) {
        speed /= 1024;
        state = state + speed.toFixed(2) + ' MB/s';
    } else {
        state = state + speed + ' KB/s';
    }
    $li.find('p.state')
        .text(state);

    $percent.css('width', percentage * 100 + '%');
});
$('#ctlBtn').click(function () {
    uploader.upload();
});