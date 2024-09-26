# 画面内操作イベント一覧

## ランドマーク一覧画面

| 操作内容 | Analytics イベント名 | パラメータ | コンバージョンイベント |
| -- | -- | -- | -- |
| いいねを付けたランドマークのみを表示するスイッチ | LandmarkListFavoritesOnlySwitch | スイッチの ON/OFF favorites_only: Int<br> |  |

## ランドマーク詳細画面

| 操作内容 | Analytics イベント名 | パラメータ | コンバージョンイベント |
| -- | -- | -- | -- |
| いいねを付けるボタン | LandmarkDetailFavoriteOn | ランドマークの ID id: Int<br>ランドマークの名前 name: String<br> | ○ |
| いいねを解除する | LandmarkDetailFavoriteOff | ランドマークの ID id: Int<br>ランドマークの名前 name: String<br> |  |

## 情報画面

| 操作内容 | Analytics イベント名 | パラメータ | コンバージョンイベント |
| -- | -- | -- | -- |
| プライバシーポリシーを表示する | InfoPrivacyPolicy |  |  |

