//
//  EvaluateViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "EvaluateViewController.h"
#import "EvalPersonalTableViewCell.h"
#import "WriteEvalViewController.h"

#import "Global.h"

@interface EvaluateViewController ()
{
}
@end

@implementation EvaluateViewController
@synthesize btnOffice, btnPersonal, imgOfficeLine, imgPersonalLine, searchBar;
@synthesize messageNumberLabel;
@synthesize bPersonal;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self onClickPersonalButton:btnPersonal];
    
    self.tblView.dataSource = self;
    self.tblView.delegate = self;
    
    bPersonal = YES;

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    // Customize Search Bar
    [searchBar setImage:[UIImage imageNamed:@"nav_search"] forSearchBarIcon:UISearchBarIconSearch state:UIControlStateNormal];
    [searchBar setBackgroundImage:[[UIImage alloc] init]];
    
    // Customize message number label
    messageNumberLabel.layer.cornerRadius = messageNumberLabel.frame.size.width / 2;
    messageNumberLabel.layer.masksToBounds = YES;
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)onClickPersonalButton:(UIButton *)sender {
    
    [btnPersonal setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
    [btnOffice setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    imgPersonalLine.hidden = NO;
    imgOfficeLine.hidden = YES;
    
    bPersonal = YES;
    
    [self.tblView reloadData];
    
}

- (IBAction)onClickOfficeButton:(UIButton *)sender {
    
    [btnOffice setTitleColor:ORANGE_COLOR forState:UIControlStateNormal];
    [btnPersonal setTitleColor:BLACK_COLOR_85 forState:UIControlStateNormal];
    imgOfficeLine.hidden = NO;
    imgPersonalLine.hidden = YES;
    
    bPersonal = NO;
    
    [self.tblView reloadData];

}

- (IBAction)onClickWriteEvaluation:(id)sender {
    WriteEvalViewController *detailViewController = [[WriteEvalViewController alloc] initWithNibName:@"WriteEvalViewController" bundle:nil];
    
    // Push the view controller.
    [self.navigationController pushViewController:detailViewController animated:YES];
}

#pragma UITableViewDelegate & UITableViewDataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 5;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 128.0f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *simpleTableIdentifier = @"EvalPersonalTableViewCell";
    EvalPersonalTableViewCell *cell = (EvalPersonalTableViewCell*)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"EvalPersonalTableViewCell" owner:self options:nil];
        cell = [nib objectAtIndex:0];
        cell.backgroundColor = [UIColor clearColor];
    }
    
    if (bPersonal) {
        [cell.imgPhoto setImage:[UIImage imageNamed:@"adapter"]];
        [cell.imgType setImage:[UIImage imageNamed:@"personal"]];
    }
    else
    {
        [cell.imgPhoto setImage:[UIImage imageNamed:@"1102"]];
        [cell.imgType setImage:[UIImage imageNamed:@"office"]];
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
   
    
}
@end
