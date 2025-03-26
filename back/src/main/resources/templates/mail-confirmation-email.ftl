<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<h2>
    ${user.firstName!"User"},
</h2>

<h4>
    ви здійснили реєстрацію в системі "<a href="${link}" style="color: #2699fb;">test</a>".
</h4>
<p>Для підтвердження адреси електронної пошти, натисніть на кнопку підтвердження:</p>

<a href="${confirmationLink}">
    <button style="
            margin: 10px 0;
            color: white;
            background-color: #368CFD;
            width: 170px;
            height: 38px;
            border-radius: 6px;
            cursor: pointer;
            border: 1px solid #368CFD;
            font-family: Segoe UI, sans-serif;
            font-size: 14px;
            font-weight: 600;">
        Підтвердити
    </button>
</a>
<p>Або перейдіть за посиланням: </p>
<a href="${confirmationLink}">${confirmationLink}</a>
<p> Якщо ви не робили подібних дій, проігноруйте цей лист.</p>
</body>
</html>
