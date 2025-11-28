<div align="center">

#  LevelUp Desktop  
### Produtividade • Foco • Hábitos • Metas • Glass UI

<table>
  <tr>
    <td><img src="https://github.com/Gzerio/levelup-desktop-java/blob/main/img/levelup.png" /></td>
    <td><img src="https://github.com/Gzerio/levelup-desktop-java/blob/main/img/levelup1.png" /></td>
  </tr>
</table>

</div>

---

##  Sobre o projeto

O **LevelUp Desktop** é um aplicativo de produtividade gamificado, com uma interface moderna em **Glassmorphism**, blur dinâmico e uma experiência inspirada em dashboards premium.

Ele reúne:

-  **Modo Foco (Pomodoro moderno)**
-  **Tarefas e hábitos**
-  **Metas com barra de progresso**
-  **Player Lo-Fi integrado**
-  **Widget de clima**
-  **Lembretes com notificações**
-  **UI com blur + bordas arredondadas**

Tudo rodando em uma janela desktop leve, fluida e 100% responsiva.

---

##  Tecnologias utilizadas

### **Frontend Desktop**
- Java **17+**
- JavaFX **21**
- FXML + CSS personalizado
- Efeito de blur via Snapshot + GaussianBlur
- Design modular usando `fx:include`

### **Backend / Integrações**
- API REST (Java Spring Boot)
- Autenticação via **JWT**
- PostgreSQL (para login real)
- OpenWeather API

---

###  Rodando via Gradle

Linux/macOS:
---
- ./gradlew run
---
Windows:
---
- gradlew.bat run
---
### Autenticação
---
O app se conecta à sua API:

- POST /auth/login → retorna JWT

- POST /auth/register → cria novo usuário

Caso a API esteja offline, o app mostra mensagem amigável.
---
## Funcionalidades atuais

- ✔️ Modo Foco com contagem regressiva
- ✔️ Player Lo-Fi completo
- ✔️ Sistema de Tarefas
- ✔️ Hábitos
- ✔️ Metas
- ✔️ Dashboard com clima
- ✔️ Lembretes com notificações desktop
- ✔️ Login e Registro reais (JWT) 

<div align="center">

Feito com ☕ + 💻 por Guilherme (Gzerio)

</div>
