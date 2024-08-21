import pandas as pd

# CSV 파일 불러오기
file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/first_peak_data.csv'
df = pd.read_csv(file_path)

# 같은 mountain_code로 묶인 데이터들 중 info에 '정상'이라는 데이터가 없는 데이터들 추출
grouped = df.groupby('MNTN_CODE')

def filter_no_peak(group):
    if '정상' not in group['info'].values:
        return group
    return pd.DataFrame()

filtered_df = grouped.apply(filter_no_peak).reset_index(drop=True)

# 필터링된 데이터프레임을 새로운 CSV 파일로 저장
filtered_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/no_peak_data.csv'
filtered_df.to_csv(filtered_file_path, index=False, encoding='utf-8-sig')

print(f"필터링된 데이터가 {filtered_file_path}에 저장되었습니다.")
