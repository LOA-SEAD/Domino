/**
 * Created by loa on 11/03/15.
 */
window.onload = function() {

    document.getElementById("p-1").onchange = function () {
        var el = $("#preview-1");
        if(!$(el).hasClass("img-responsive")) {
            preview(this, document.getElementById("preview-1"));
        }
    };

    document.getElementById("p-2").onchange = function () {
        var el = $("#preview-2");
        if(!$(el).hasClass("img-responsive")) {
            preview(this, document.getElementById("preview-2"));
        }
    };

    document.getElementById("p-3").onchange = function () {
        var el = $("#preview-3");
        if(!$(el).hasClass("img-responsive")) {
            preview(this, document.getElementById("preview-3"));
        }
    };

    document.getElementById("p-4").onchange = function () {
        var el = $("#preview-4");
        if(!$(el).hasClass("img-responsive")) {
            preview(this, document.getElementById("preview-4"));
        }
    };

    document.getElementById("p-5").onchange = function () {
        var el = $("#preview-5");
        if(!$(el).hasClass("img-responsive")) {
            preview(this, document.getElementById("preview-5"));
        }
    };

    document.getElementById("p-6").onchange = function () {
        var el = $("#preview-6");
        if(!$(el).hasClass("img-responsive")) {
            preview(this, document.getElementById("preview-6"));
        }
    };

    document.getElementById("p-7").onchange = function () {
        var el = $("#preview-7");
        if(!$(el).hasClass("img-responsive")) {
            preview(this, document.getElementById("preview-7"));
        }
    };

    function verifyDimensions(input) {
        var file;
        var inputs = document.getElementsByTagName('input');
        if ((file = input.files[0])) {
            console.log(input.files[0]);
            image = new Image();
            image.src = window.URL.createObjectURL(file);
            console.log("antes de entrar no onload da imagem");
            image.onload = function () {
                console.log("The image width is " + image.width + " and image height is " + image.height);
                if ((image.width < 800) || (image.height < 600)) {
                        alert("Alguma das imagens contém tamanho invalido. Resolução mínima: 800x600");
                       input.setAttribute('data-image','false');
                        image.pop;
                }
                else{
                    if(input.getAttribute('data-image') == 'false'){
                        input.setAttribute('data-image','true');
                        image.pop;
                    }
                    else {
                        input.setAttribute('data-image','true');
                        image.pop;
                    }
                }

                for(var i=0; i<inputs.length; i++) {
                    var data = inputs[i].getAttribute('data-image');
                    console.log("Data: "+data);
                    if (data == 'false') {
                        $('#upload').prop('disabled', true);
                        break;
                    }
                    else
                        $('#upload').prop('disabled', false);
                }
            }

        }

    }

    function preview(input, preview) {
        var oFReader = new FileReader();

        oFReader.readAsDataURL($(input).prop("files")[0]);

        oFReader.onload = function (oFREvent) {
            preview.src = oFREvent.target.result;
        };


    }
}


