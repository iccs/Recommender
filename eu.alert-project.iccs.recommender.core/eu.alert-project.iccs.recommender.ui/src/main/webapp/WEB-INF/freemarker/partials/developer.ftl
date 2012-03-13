<div id="bugs-container">
<ul class="bugs-list">
<#list developer as bug>
    <li>
        <table class="bug">
            <thead>
                <tr>
                    <th>${bug.id}</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td >${bug.subject} ${bug.description}</td>
                </tr>
            </tbody>
        </table>
    </li>
</#list>
</ul>
</div>


