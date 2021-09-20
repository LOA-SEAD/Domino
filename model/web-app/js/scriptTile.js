/**
 * Created by matheus on 4/29/15.
 */

$(document).ready(function(){
    $('.collapsible').collapsible({
        accordion : false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
    });
});


window.onload = function(){
    console.log("ok");

    var checkboxes = document.getElementsByTagName('input');

    for (var i=0; i<checkboxes.length; i++)  {
        if (checkboxes[i].type == 'radio')   {
            checkboxes[i].checked = false;
        }
    }

    $(".save").click(function() {
        var select = false;
        var checkboxes = document.getElementsByTagName('input');
        var i = 0;
        var counter = 0;
        var ids = "";
        var first = true;
        while(i < checkboxes.length){
            if ((checkboxes[i].type == 'checkbox') && (checkboxes[i].checked == true)){
                counter++;
                if (first) {
                    ids += "?ids=" + checkboxes[i].value
                    first = false
                } else {
                    ids += "&ids=" + checkboxes[i].value
                }
            }
            i++;
        }
        if(counter != 7){
            alert(counter + " faces foram selecionadas. Você deve selecionar 7 faces antes de enviar.");
        }
        else{
            window.top.location.href = "/domino/tile/choose/" + ids;
        }
    });


    $(".delete").click(function() {
        var tr = $(this).parent().parent();
        var id = $(tr).attr("data-id");
        var data = { _method: 'DELETE' };


        if(confirm("Deseja realmente excluir este tema?")) {
            $.ajax({
                type: 'POST',
                data: data,
                url: "delete/" + id,
                success: function (data) {
                    console.log(data);
                    $(tr).hide();
                    $(tr).remove();

                    var myThemes = document.getElementsByClassName("myTheme");
                    if(myThemes.length==0){
                        window.location.reload();
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        }
    });

    var doors = $(".door");

    $(doors).mouseover(function() {
        var src =  $(this).attr('src');
        console.log(src);
        src = src.replace("sheet1", "sheet0");
        console.log(src);
        $(this).attr("src", src);
    });

    $(doors).mouseout(function() {
        var src =  $(this).attr('src');
        console.log(src);
        src = src.replace("sheet0", "sheet1");
        console.log(src);
        $(this).attr("src", src);
    })

};