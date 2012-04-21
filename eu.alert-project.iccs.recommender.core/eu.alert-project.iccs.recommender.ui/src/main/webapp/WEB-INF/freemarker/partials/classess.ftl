<div id="classes-container">

<h1>Classes</h1>
<h3><em>Select Type of Developer</em></h3>
<ul class="classess-list">
<#list classess as clazz>
    <li>
        <label for="check-${clazz}">${clazz}</label>
        <input
            type="checkbox"
            id="check-${clazz}"
            name="${clazz}"
            value="${clazz}"
            onclick="toggleSelected('${clazz}');"
            />
    </li>
</#list>
</ul>
</div>


