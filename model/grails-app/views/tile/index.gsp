<%@ page import="br.ufscar.sead.loa.domino.remar.Tile" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:external dir="css" file="themes.css"/>
    <g:javascript src="scriptTile.js"/>
    <g:javascript src="iframeResizer.contentWindow.min.js"/>
    <!--<g:set var="entityName" value="${message(code: 'Tile.label', default: 'Tile')}"/>-->

</head>

<body>

<div class="cluster-header">
    <p class="text-teal text-darken-3 left-align margin-bottom" style="font-size: 28px;">
        Dominó - Faces
    </p>
</div>

<div class="row">
    <form class="col s12" name="formName">
        <ul class="collapsible" data-collapsible="accordion">
            <li>
                <div class="collapsible-header active"><b>Minhas Faces</b></div>

                <div class="collapsible-body">
                    <g:if test="${tileInstanceList.size() < 1}">
                        <p>Você ainda não possui nenhuma face</p>
                    </g:if>
                    <g:else>
                        <table class="" id="tableMyTile">
                            <thead>
                            <tr>
                                <th class="simpleTable">Selecionar</th>
                                <th class="simpleTable">Descrição</th>
                                <th class="simpleTable">Peça 1</th>
                                <th class="simpleTable">Peça 2</th>
                                <th class="simpleTable">Peça 3</th>
                                <th class="simpleTable">Peça 4</th>
                                <th class="simpleTable">Peça 5</th>
                                <th class="simpleTable">Peça 6</th>
                                <th class="simpleTable">Peça 7</th>
                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${tileInstanceList}" status="i" var="tileInstance">

                                <tr data-id="${tileInstance.id}" class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                    <td class="myTile simpleTable" align="center ">
                                        <input class="with-gap " name="radio"
                                               type="checkbox" id="myTile${i}"
                                               value="${tileInstance.id}">
                                        <label for="myTile${i}"></label>
                                    </td>
                                    <td align="center">${fieldValue(bean: tileInstance, field: "description")}</td>

                                    <td align="center"><img width="56"
                                                            src="/domino/data/${tileInstance.ownerId}/tiles/${tileInstance.id}/0.png"
                                                            class="img-responsive max"/></td>
                                    <td align="center"><img width="56"
                                                            src="/domino/data/${tileInstance.ownerId}/tiles/${tileInstance.id}/1.png"
                                                            class="img-responsive max"/></td>
                                    <td align="center"><img width="56"
                                                            src="/domino/data/${tileInstance.ownerId}/tiles/${tileInstance.id}/2.png"
                                                            class="img-responsive max"/></td>
                                    <td align="center"><img width="56"
                                                            src="/domino/data/${tileInstance.ownerId}/tiles/${tileInstance.id}/3.png"
                                                            class="img-responsive max"/></td>
                                    <td align="center"><img width="56"
                                                            src="/domino/data/${tileInstance.ownerId}/tiles/${tileInstance.id}/4.png"
                                                            class="img-responsive max"/></td>
                                    <td align="center"><img width="56"
                                                            src="/domino/data/${tileInstance.ownerId}/tiles/${tileInstance.id}/5.png"
                                                            class="img-responsive max"/></td>
                                    <td align="center"><img width="56"
                                                            src="/domino/data/${tileInstance.ownerId}/tiles/${tileInstance.id}/6.png"
                                                            class="img-responsive max"/></td>
                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </g:else>
                </div>
            </li>
        </ul>

        <div class="row">
            <div class="col s12">
                <g:submitButton name="save" class="save btn btn-success btn-lg my-orange" value="Enviar"/>
                <g:link class="btn btn-success btn-lg my-orange" action="create">Nova face</g:link>
            </div>
        </div>
    </form>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.6/js/materialize.min.js"></script>

</body>
</html>
