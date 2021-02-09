<h1 align="center">Obfuscated</h1>

> $*)(*$#)@*#$)_@*#_$ 

### 필수 사항
- Bukkit 1.16.4 - 1.16.5

### 설명

![HotBar](/images/hotbar.png)<br>
오프핸드를 제외한 플레이어의 핫바에 있는 아이템 중 특정 아이템을 무작위로 같은 종류의 아이템과 교체합니다.

기본적으로 양털, 색유리, 색유리 판, 양탄자, 테라코타, 유광 테라코타, 침대, 셜커 상자, 콘크리트, 콘크리트 가루, 현수막, 염료, 생성 알, 포션, 투척용 포션, 잔류형 포션, 화살, 마법이 부여된 책, 음반이 포함됩니다.
설정을 통해 포함시킬 종류를 선택할 수 있습니다.

__plugins/Obfuscated/config.yml__ 파일에서 플러그인의 설정 정보를 확인하고 수정할 수 있습니다.

```yml
# 플러그인의 활성화 여부 (true/false)
enable: true

# 아이템 교체 딜레이 (자연수)
delay: 1

# 아이템 교체에 포함시킬 종류 (리스트)
# - all이 포함되었다면 모든 종류를 선택합니다
# - 선택 가능한 항목 : wool, stained_glass, stained_glass_pane, carpet, terracotta, glazed_terracotta, bed, shulker_box, concrete, concrete_powder, banner, dye, spawn_egg, potion, splash_potion, lingering_potion, tipped_arrow, enchanted_book, music_disc
allowed-items:
    - all
```

### 명령어

플러그인 활성화 :
```/o enable```

플러그인 비활성화 :
```/o disable```

아이템 교체 딜레이 설정 :
```/o delay <틱:자연수>```

설정 리로드 :
```/o reload```

### 크레딧
- GitHub [COIN-KR](https://github.com/COIN-KR)
- GitHub [dacoonkr](https://github.com/dacoonkr)
- 초기 아이디어 제공 [마플 마인크래프트 채널 - 너의 아이템이 보여(마인크래프트)](https://www.youtube.com/watch?v=C2KARkQc91g)
