#my_array=("1-a-add" "1-b-sub" "1-c-mul" "1-d-div" "1-e-expressions")
my_array=("1-a-add" "1-a-add")

sum=0

# Loop through each element
for item in "${my_array[@]}"; do
	echo $item
	cd $item
	result=$(sh make.sh)
	sum=$((sum + result))
	cd ..
done



echo failures = $sum
