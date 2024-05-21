#include<iostream>
#include<vector>
#include<stack>

using namespace std;

int main(){
    int N, M;
    cin >> N >> M;

    vector<vector<int> > graph_student(N, vector<int>(N, 0));
    vector<vector<int> > graph_student_inverse(N, vector<int>(N, 0));
    vector<vector<bool> > graph_visit(N, vector<bool>(N, 0));

    int num1, num2;
    for (int i = 0; i < M; i++) {
        cin >> num1 >> num2;
        graph_student[num1 - 1][num2 - 1] = 1;
        graph_student_inverse[num2 - 1][num1 - 1] = 1;
    }

    stack<int> back_tracking;

    // DFS
    for (int i = 0; i < N; i++) {
        back_tracking.push(i);
        graph_visit[i][i] = true;
        while (!back_tracking.empty()) {
            int current = back_tracking.top();
            int next = current;
            for (int j = 0; j < N; j++) {
                if (graph_student[current][j] != 0 && graph_visit[i][j] == false) {
                    next = j;
                    back_tracking.push(next);
                    graph_visit[i][next] = true;
                    break;
                }
            }
            if (next == current) {
                back_tracking.pop();
            }
        }
    }

    // DFS_inverse
    for (int i = 0; i < N; i++) {
        back_tracking.push(i);
        graph_visit[i][i] = true;
        while (!back_tracking.empty()) {
            int current = back_tracking.top();
            int next = current;
            for (int j = 0; j < N; j++) {
                if (graph_student_inverse[current][j] == 1 && graph_visit[i][j] == false) {
                    next = j;
                    back_tracking.push(next);
                    graph_visit[i][next] = true;
                    break;
                }
            }
            if (next == current) {
                back_tracking.pop();
            }
        }
    }

    int num = 0;
    bool flag;
    for (int i = 0; i < N; i++) {
        flag = true;
        for (int j = 0; j < N; j++) {
            if (graph_visit[i][j] == false) {
                flag = false;
                break;
            }
        }
        if (flag == true) {
            num++;
        }
    }
    cout << num << endl;

    return 0;
}
