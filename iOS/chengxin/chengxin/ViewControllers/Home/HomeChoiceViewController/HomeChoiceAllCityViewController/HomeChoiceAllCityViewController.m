//
//  HomeChoiceAllCityViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeChoiceAllCityViewController.h"
#import "HomeChoiceCityTableViewCell.h"
#import "Global.h"

@interface HomeChoiceAllCityViewController ()

@end

@implementation HomeChoiceAllCityViewController
@synthesize choiceCityTableView;
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [choiceCityTableView registerNib:[UINib nibWithNibName:@"HomeChoiceCityTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeChoiceCityCellIdentifier"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - IBAction
- (IBAction)cancelAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)selectAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - TableView
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HomeChoiceCityTableViewCell *homeChoiceCityTableCell = (HomeChoiceCityTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"HomeChoiceCityCellIdentifier" forIndexPath:indexPath];
    [homeChoiceCityTableCell.cityNameLabel setText:[CityArray objectAtIndex:indexPath.row]];
    return homeChoiceCityTableCell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return CityArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 45.f;
    return homeTableCellHeight;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSArray<NSString *> *)sectionIndexTitlesForTableView:(UITableView *)tableView {
    return AlphabetsArray;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return [AlphabetsArray objectAtIndex:section];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
