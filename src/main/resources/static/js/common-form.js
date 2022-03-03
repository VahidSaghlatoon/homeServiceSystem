$(document).ready(function () {
    $("#fileImage").change(function () {
        var fileSize = this.files[0].size;
        // alert("FileSize" + fileSize);
        if (fileSize > 307200) {
            this.setCustomValidity("سایز تصویر باید از  300kb کمتر باشد");
            this.reportValidity();
        } else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
    });
});

function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
}
