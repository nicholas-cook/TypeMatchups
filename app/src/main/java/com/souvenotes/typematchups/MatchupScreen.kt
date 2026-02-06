package com.souvenotes.typematchups

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.souvenotes.typematchups.ui.theme.EkansLightPurple
import com.souvenotes.typematchups.ui.theme.EkansLightYellow
import com.souvenotes.typematchups.ui.theme.TypeMatchupsTheme

@Composable
fun TypeMatchupScreen(
    screenState: TypeMatchupScreenState,
    onEvent: (TypeMatchupEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(topBar = { TypeMatchupsTopAppBar() }) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.title_target_pokemon),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DropdownField(
                        modifier = Modifier.weight(1f),
                        selectedType = screenState.pokemonType1,
                        options = screenState.type1Options,
                        labelText = stringResource(R.string.label_type1),
                        onOptionSelected = { onEvent(TypeMatchupEvent.PokemonType1Selected(it)) }
                    )
                    DropdownField(
                        modifier = Modifier.weight(1f),
                        selectedType = screenState.pokemonType2,
                        options = screenState.type2Options,
                        labelText = stringResource(R.string.label_type2),
                        onOptionSelected = { onEvent(TypeMatchupEvent.PokemonType2Selected(it)) },
                        enabled = screenState.type2Enabled
                    )
                }
                Spacer(modifier = Modifier.size(48.dp))
            }
            when (screenState.effectiveness) {
                EffectivenessState.Empty -> {}
                is EffectivenessState.Error -> item {
                    Text(
                        text = stringResource(R.string.description_error),
                        fontSize = 48.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                EffectivenessState.Loading -> item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is EffectivenessState.Success -> {
                    item {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(R.string.title_types),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = stringResource(R.string.title_effectiveness),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    items(screenState.effectiveness.matchups.toList()) { (title, types) ->
                        EffectivenessSection(
                            effectiveness = title,
                            types = types
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    modifier: Modifier,
    selectedType: DisplayPokemonType,
    options: List<DisplayPokemonType>,
    labelText: String,
    onOptionSelected: (DisplayPokemonType) -> Unit,
    enabled: Boolean = true
) {
    var isExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { if (enabled) isExpanded = it },
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .background(
                    color = selectedType.backgroundColor,
                    shape = RoundedCornerShape(4.0.dp)
                ),
            value = stringResource(selectedType.displayString),
            onValueChange = {},
            readOnly = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors().copy(
                focusedContainerColor = selectedType.backgroundColor,
                unfocusedContainerColor = selectedType.backgroundColor,
                focusedTextColor = if (selectedType == DisplayPokemonType.NONE) Color.Black else Color.White,
                unfocusedTextColor = if (selectedType == DisplayPokemonType.NONE) Color.Black else Color.White,
                disabledContainerColor = selectedType.backgroundColor,
                disabledTextColor = if (selectedType == DisplayPokemonType.NONE) Color.Black else Color.White
            ),  
            singleLine = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = modifier.rotate(if (isExpanded) 180f else 0f),
                    tint = if (selectedType == DisplayPokemonType.NONE) Color.Black else Color.White
                )
            },
            enabled = enabled,
            label = {
                Text(
                    text = labelText,
                    color = if (selectedType == DisplayPokemonType.NONE) Color.Black else Color.White
                )
            },
            textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            containerColor = EkansLightPurple
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    modifier = Modifier.background(color = option.backgroundColor),
                    text = {
                        Text(
                            text = stringResource(option.displayString),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    onClick = {
                        isExpanded = false
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeMatchupsTopAppBar() {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = EkansLightYellow,
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black
        )
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EffectivenessSection(effectiveness: String, types: List<DisplayPokemonType>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 4.dp,
                alignment = Alignment.CenterVertically
            ),
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(8.dp)
        ) {
            types.forEach { type ->
                TypePill(type = type)
            }
        }
        val split = effectiveness.split("/")
        val effectivenessString = buildAnnotatedString {
            if (split.size == 1) {
                append(effectiveness)
                append("x")
                return@buildAnnotatedString
            }
            withStyle(
                style = SpanStyle(
                    baselineShift = BaselineShift.Superscript,
                    fontSize = 24.sp
                )
            ) {
                append(split[0])
            }
            withStyle(style = SpanStyle(fontSize = 24.sp)) {
                append("/")
            }
            withStyle(
                style = SpanStyle(
                    baselineShift = BaselineShift.Subscript,
                    fontSize = 24.sp
                )
            ) {
                append(split[1])
            }
            append("x")
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(text = effectivenessString, fontSize = 48.sp)
        }
    }
}

@Composable
fun TypePill(type: DisplayPokemonType) {
    Box(
        modifier = Modifier
            .background(color = type.backgroundColor, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .width(64.dp)
    ) {
        Text(
            text = stringResource(type.displayString),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EffectivenessSectionPreview() {
    TypeMatchupsTheme {
        EffectivenessSection(
            effectiveness = "2",
            types = listOf(
                DisplayPokemonType.BUG,
                DisplayPokemonType.DARK,
                DisplayPokemonType.DRAGON
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TypeMatchupScreenPreview() {
    TypeMatchupsTheme {
        TypeMatchupScreen(
            screenState = TypeMatchupScreenState(effectiveness = EffectivenessState.Loading),
            onEvent = {}
        )
    }
}
