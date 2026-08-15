# TOEIC App

TOEIC Reading (Part5 / Part6 / Part7) の練習問題を解けるWebアプリです。
Spring Boot + Thymeleaf + H2(インメモリDB)で作られています。

## 使い方

```bash
./mvnw spring-boot:run
```

起動すると、Spring Bootが自動でH2インメモリDBを作成しサンプルデータを読み込みます。DBへの手動接続などは不要です。そのままブラウザで http://localhost:8080 を開けば使えます(このPC上でアプリが起動している間のみ有効なローカル環境です)。

1. Passage一覧から問題を選ぶ
2. 選択肢から回答を選んで「採点する」を押す
3. 正解/不正解と解説が表示される

## 構成

- `model` — `Passage` (長文) / `Question` (設問) / `Choice` (選択肢) のJPAエンティティ
- `repository` — Spring Data JPAリポジトリ
- `controller` — 一覧表示・出題・採点を行うThymeleafコントローラー(`PassageController`)
- `resources/templates` — 一覧・出題・結果画面のHTMLテンプレート
- `resources/data.sql` — サンプルデータ(Part5/6/7それぞれ1passage)

## H2コンソール(任意・デバッグ用)

アプリの利用自体には不要ですが、DBの中身を直接確認したい場合に使えます:

- URL: http://localhost:8080/h2-console (アプリ起動中のみアクセス可)
- JDBC URL: `jdbc:h2:mem:toeicdb`
- User: `sa` (パスワードは空欄)

`passage` / `question` / `choice` テーブルにサンプルデータが入っています。

## 今後の課題

- Part7の複数設問(1つのpassageに複数問)対応
- スコア履歴・間違えた問題の復習機能
