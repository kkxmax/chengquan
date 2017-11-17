//
//  HomeChoiceBusinessViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeChoiceBusinessViewController.h"
#import "HomeChoiceBusinessTableViewCell.h"

@interface HomeChoiceBusinessViewController ()
{
    UIView *extendView;
}
@end

@implementation HomeChoiceBusinessViewController
@synthesize businessTableView;
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [businessTableView registerNib:[UINib nibWithNibName:@"HomeChoiceBusinessTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeChoiceBusinessCellIdentifier"];
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

#pragma mark - UITableViewDelegate, UITableViewDataSource
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HomeChoiceBusinessTableViewCell *homeChoiceBusinessTableCell = (HomeChoiceBusinessTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"HomeChoiceBusinessCellIdentifier" forIndexPath:indexPath];
    if(indexPath.row == 0) {
        homeChoiceBusinessTableCell.extendButton.hidden = YES;
        homeChoiceBusinessTableCell.networkContentButton.hidden = YES;
        homeChoiceBusinessTableCell.networkNameLabel.text = @"不限";
    }else{
        if(homeChoiceBusinessTableCell.extendButton.isSelected) {
            extendView = homeChoiceBusinessTableCell.networkContentView;
        } else {
            extendView = nil;
        }
    }
    homeChoiceBusinessTableCell.contentTableView = businessTableView;
    return homeChoiceBusinessTableCell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 5;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 45.f;
    if(extendView)
        homeTableCellHeight += 55.f;
    return homeTableCellHeight;
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
