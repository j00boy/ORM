import pandas as pd

# CSV 파일 불러오기
file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240802/final_peak_data.csv'
df = pd.read_csv(file_path)

# 'info' 열 초기화
df['info'] = df['info'].fillna('')

# 각 mountain_code 그룹 내에서 '시종점'이 아닌 좌표 중 가장 높은 elevation 값을 가진 행에 '정상' 값 추가
def add_peak_label(group):
    non_terminal_points = group[group['info'] != '시종점']
    if not non_terminal_points.empty:
        max_elevation_index = non_terminal_points['Elevation'].idxmax()
        group.at[max_elevation_index, 'info'] = '정상'
    return group

df = df.groupby('MNTN_CODE').apply(add_peak_label)

# 업데이트된 데이터프레임을 새로운 CSV 파일로 저장
updated_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240802/all_peak_data.csv'
df.to_csv(updated_file_path, index=False, encoding='utf-8-sig')

print(f"업데이트된 데이터가 {updated_file_path}에 저장되었습니다.")
