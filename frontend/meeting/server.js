const express = require("express");
const path = require("path");

const app = express();

app.set("port", process.env.PORT || 3000);

// express에서 정적 파일 제공 설정
app.use(express.static(path.join(__dirname, "build")));

// 루트 요청을 처리하고, React 애플리케이션의 빌드된 index.html 파일을 제공
app.get("/", (req, res) => {
  res.sendFile(path.join(__dirname, "build", "index.html"));
});

// 서버 실행
app.listen(app.get("port"), () => {
  console.log(`${app.get("port")}번 포트에서 대기중..`);
});
