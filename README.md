# TOEIC App

TOEICの練習問題を解けるWebアプリです。Reading (Part5 / Part6 / Part7) に加えて、Listening (Part3) にも対応しています。
Spring Boot + Thymeleaf + H2(ファイル保存DB)+ Spring Securityで作られています。

## 使い方

```bash
./mvnw spring-boot:run
```

起動すると、Spring Bootが自動でH2データベース(`./data/toeicdb`)を作成・初期化し、サンプルデータとログイン用ユーザーを投入します。ブラウザで http://localhost:8080 を開き、以下でログインしてください(このPC上でアプリが起動している間のみ有効なローカル環境です)。

- ユーザー名: `Taro`
- パスワード: `pass`

1. Passage一覧から問題を選ぶ(Reading: Part5/6/7、Listening: Part3)
2. Listening問題は音声を再生してから回答する
3. 選択肢から回答を選んで「採点する」を押す
4. 正解/不正解と解説(Listeningの場合はスクリプトも)が表示される
5. 間違えた問題は「復習する」からまとめて解き直せる(ログインユーザーごとに記録され、ログアウトしても消えない)

## 構成

- `model` — `Passage` (長文/会話) / `Question` (設問) / `Choice` (選択肢) / `User` / `MissedQuestion` のJPAエンティティ
- `repository` — Spring Data JPAリポジトリ
- `controller` — 一覧表示・出題・採点・復習を行うThymeleafコントローラー(`PassageController` / `ReviewController`)
- `config` — Spring Securityの認証設定(`SecurityConfig`)とサンプルデータ/ユーザーの投入(`DataSeedConfig`)
- `resources/templates` — 一覧・出題・結果・復習画面のHTMLテンプレート
- `resources/static/audio` — Listening問題の音声ファイル(mp3)

## Listening音声について

音声は[edge-tts](https://github.com/rany2/edge-tts)(無料のTTSツール)で事前生成したものを静的ファイルとして同梱しています。アプリ実行時にPython等は一切不要です。

## H2コンソール(任意・デバッグ用)

アプリの利用自体には不要ですが、DBの中身を直接確認したい場合に使えます:

- URL: http://localhost:8080/h2-console (アプリ起動中のみアクセス可)
- JDBC URL: `jdbc:h2:file:./data/toeicdb`
- User: `sa` (パスワードは空欄)

## 今後の課題

- Listening Part1/2/4への対応
- スコア履歴の可視化
