import pandas as pd

# 엑셀 파일 불러오기
file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/startAndPeak.csv'
df = pd.read_csv(file_path)

# 'Entry_Point' 컬럼이 '시종점'인 데이터만 필터링
start_df = df[df['Entry_Point'] == '시종점']

# 'Entry_Point' 컬럼이 '정상'인 데이터만 필터링
peak_df = df[df['Entry_Point'] == '정상']

# MNTN_CODE별로 하나씩만 추출 (첫 번째 데이터 선택)
unique_peak_df = peak_df.drop_duplicates(subset='MNTN_CODE', keep='first')

# 두 데이터프레임 결합
combined_df = pd.concat([start_df, unique_peak_df])

# 추출된 데이터를 새로운 엑셀 파일로 저장
output_file_path = 'C:/Users/SSAFY/Desktop/ORM_data_parsing/240804/filtered_startAndPeak.csv'
combined_df.to_csv(output_file_path, index=False, encoding='utf-8-sig')

print(f"Filtered data saved to {output_file_path}")
