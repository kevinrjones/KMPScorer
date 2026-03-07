package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cricket.knowledgespike.scorer.foundation.compose.moveFocusOnTab
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.opponents_label
import kmpscorer.shared.generated.resources.search
import kmpscorer.shared.generated.resources.team_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun FindMatches(
    onSearchQueryChange: (String, String) -> Unit,
    onImeSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var teamName by remember { mutableStateOf("") }
    var opponentsName by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Scaffold(
    ) { innerPadding ->
        Surface(
        modifier = modifier.padding(innerPadding)
            .padding(start = 16.dp, end = 16.dp),
        ) {
            Column {
                Row() {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .moveFocusOnTab(focusManager),
                        label = {
                            Text(stringResource(Res.string.team_label))
                        },
                        value = teamName,
                        onValueChange = {
                            teamName = it
                        },
                        isError = false,
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .moveFocusOnTab(focusManager),
                        label = {
                            Text(stringResource(Res.string.opponents_label))
                        },
                        value = opponentsName,
                        onValueChange = {
                            opponentsName = it
                        },
                        isError = false,
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.width(150.dp),
                        onClick = {
                            onSearchQueryChange(teamName, teamName)
                            onImeSearch()
                        }
                    ) {
                        Text(stringResource(Res.string.search))
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun FindMatchesPreview() {
    FindMatches(onSearchQueryChange = { _, _ -> }, onImeSearch = {})
}