def calculate_total_time (activities, activity):
    total_time = 0
    for i in activities[activity]:
        total_time += i
    return total_time

def next_operation(activities):
    operation_type = input('Please choose operation type (add (a) / show (s) / top (t) / quit (q)): ')
    if operation_type == 'a' or operation_type == 'add':
        activity = input('Enter the activity name: ')
        time = int(input('Enter the time (in minutes): '))
        if activity in activities:
            activities[activity].append(time)
        else:
            activities[activity] = [time]
    elif operation_type == 's' or operation_type == 'show':
        activity = input('Enter the activity name: ')
        if activity not in activities:
            print('Activity not found!')
        else:
            total_time = calculate_total_time(activities, activity)
            print("You've spent a total of ", total_time, ' minutes doing ' + activity)
    elif operation_type == 't' or operation_type == 'top':
        activities_with_total_time = [(calculate_total_time(activities, activity), activity) for activity in activities]
        sorted_activities = sorted(activities_with_total_time, reverse=True)
        print('Your top activities are:')
        printed_activities = 0
        for activity in sorted_activities:
            print(activity[1], ': ', activity[0], ' minutes')
            printed_activities += 1
            if printed_activities == 3:
                break
    elif operation_type == 'q' or operation_type == 'quit':
        print('Exiting the program...')
        return
    else:
        print('Wrong operation type!')
    next_operation(activities)

def main():
    activities = {}
    next_operation(activities)

if __name__ == '__main__':
    main()